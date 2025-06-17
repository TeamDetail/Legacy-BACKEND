package com.learnmore.legacy.domain.Kakaooauth.service;

import com.learnmore.legacy.domain.Kakaooauth.error.KaKaoAuthError;
import com.learnmore.legacy.domain.Kakaooauth.presentation.dto.response.KakaoInfo;
import com.learnmore.legacy.domain.Kakaooauth.presentation.dto.response.KakaoTokenRes;
import com.learnmore.legacy.domain.auth.presentation.dto.response.TokenRes;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.enums.UserRole;
import com.learnmore.legacy.domain.user.service.UserService;
import com.learnmore.legacy.global.config.KakaoConfig;
import com.learnmore.legacy.global.exception.CustomException;
import com.learnmore.legacy.global.security.jwt.JwtProvider;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoConfig kakaoConfig;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final WebClient webClient = WebClient.create("https://kauth.kakao.com");

    @Transactional
    public TokenRes loginWithKakaoApp(TokenRes req) {
        KakaoInfo userInfo = getUserInfo(req.accessToken());
        upsertUser(userInfo);
        return jwtProvider.generateToken(userInfo.getId().toString());

    }

    @Transactional
    public TokenRes loginWithKakao(String code){
        KakaoTokenRes token = getAccessToken(code);
        KakaoInfo userInfo = getUserInfo(token.access_token());
        upsertUser(userInfo);
        return jwtProvider.generateToken(userInfo.getId().toString());
    }

    private KakaoTokenRes getAccessToken(String code) {
        return webClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("grant_type", "authorization_code")
                        .with("client_id", kakaoConfig.getClientId())
                        .with("redirect_uri", kakaoConfig.getWebRedirectUri())
                        .with("code", code)
                        .with("client_secret", kakaoConfig.getClientSecret()))
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.out.println("카카오 토큰 요청 실패 - 상태코드: " + clientResponse.statusCode());
                                    System.out.println("응답 본문: " + errorBody);
                                    return Mono.error(new CustomException(KaKaoAuthError.TOKEN_REQUEST_FAILED, errorBody));
                                })
                )
                .bodyToMono(KakaoTokenRes.class)
                .block(Duration.ofSeconds(5));
    }

    public KakaoInfo getUserInfo(String accessToken) {
        JsonNode jsonNode = webClient
                .post()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody ->
                                        Mono.error(new CustomException(KaKaoAuthError.IN_FO_REQUEST_FAILED, errorBody))
                                )
                )
                .bodyToMono(JsonNode.class)
                .block(Duration.ofSeconds(5));

        if (jsonNode == null) {
            throw new CustomException(KaKaoAuthError.KAKAO_RESPONSE_EMPTY);
        }

        long id = jsonNode.path("id").asLong();
        String nickname = jsonNode.path("properties").path("nickname").asText(null);
        String profileImage = jsonNode.path("properties").path("profile_image").asText(null);

        if (nickname == null || profileImage == null) {
            throw new CustomException(KaKaoAuthError.KAKAO_RESPONSE_EMPTY,jsonNode.toPrettyString());
        }

        return new KakaoInfo(id, nickname, profileImage);
    }


    private void upsertUser(KakaoInfo userInfo) {
        if (userService.existsById(userInfo.getId())){
            updateUser(userInfo);
        }else {
            saveUser(userInfo);
        }
    }

    private void saveUser(KakaoInfo kakaoUser) {
        User user = User.builder()
                .userId(kakaoUser.getId())
                .nickname(kakaoUser.getNickname())
                .level(1)
                .exp(0)
                .credit(0)
                .snowflakeCapacity(5)
                .storeRestock(1)
                .creditCollect(3)
                .dropCount(1)
                .role(UserRole.USER)
                .allBlocks(0)
                .ruinsBlocks(0)
                .maxFloor(0)
                .maxScore(0)
                .imageUrl(kakaoUser.getProfileImage())
                .build();
        userService.save(user);

    }

    private void updateUser(KakaoInfo kakaoUser) {

        //todo base time 엔티티 만들기
    }
}
