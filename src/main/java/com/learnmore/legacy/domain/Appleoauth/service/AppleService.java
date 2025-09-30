package com.learnmore.legacy.domain.Appleoauth.service;

import com.learnmore.legacy.domain.Appleoauth.AppleJwtParser;
import com.learnmore.legacy.domain.Appleoauth.presentation.dto.request.AppleCodeReq;
import com.learnmore.legacy.domain.Appleoauth.presentation.dto.request.AppleIdTokenReq;
import com.learnmore.legacy.domain.Appleoauth.presentation.dto.response.AppleInfo;
import com.learnmore.legacy.domain.Appleoauth.presentation.dto.response.AppleTokenRes;
import com.learnmore.legacy.domain.auth.presentation.dto.response.TokenRes;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.enums.UserRole;
import com.learnmore.legacy.domain.user.service.UserService;
import com.learnmore.legacy.global.properties.AppleProperties;
import com.learnmore.legacy.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AppleService {

    private final AppleProperties appleProperties;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AppleJwtParser appleJwtParser;

    private final WebClient webClient = WebClient.create("https://appleid.apple.com");


    @Transactional
    public TokenRes loginApple(AppleCodeReq req) {
        // 토큰 요청
        AppleTokenRes token = getAccessToken(req.code());

        // id_token 검증 & 유저 정보 추출
        AppleInfo userInfo = appleJwtParser.parseIdentityToken(token.getIdToken(), req.name());

        userInfo.setFullName(req.name());

        // 사용자 upsert
        upsertUser(userInfo);

        // JWT 발급
        return jwtProvider.generateToken(userInfo.getSub());
    }

    @Transactional
    public TokenRes loginAppleApp(AppleIdTokenReq req) {
        // 1. id_token 검증
        AppleInfo userInfo = appleJwtParser.parseIdentityToken(req.idToken(), req.name());

        // 2. 로그인 시 fullName 추가
        userInfo.setFullName(req.name());

        // 3. 사용자 upsert
        upsertUser(userInfo);

        // 4. JWT 발급
        return jwtProvider.generateToken(userInfo.getSub());
    }


    private AppleTokenRes getAccessToken(String code) {
        return webClient.post()
                .uri("/auth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("grant_type", "authorization_code")
                        .with("client_id", appleProperties.getClientId())
                        .with("client_secret", appleProperties.generateClientSecret())
                        .with("code", code)
                        .with("redirect_uri", appleProperties.getRedirectUri()))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("Apple 토큰 요청 실패: " + errorBody)))
                )
                .bodyToMono(AppleTokenRes.class)
                .block(Duration.ofSeconds(5));
    }

    private void upsertUser(AppleInfo userInfo) {
        if (userService.existsByUserId(convertSubToLong(userInfo.getSub()))) {
            updateUser(userInfo);
        } else {
            saveUser(userInfo);
        }
    }

    private void saveUser(AppleInfo appleUser) {
        User user = User.builder()
                .userId(convertSubToLong(appleUser.getSub()))
                .nickname(appleUser.getFullName())
                .description("")
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
                // 애플에서 제공안해서 기본값으로 설정
                .imageUrl("http://img1.kakaocdn.net/thumb/R640x640.q70/?fname=http://t1.kakaocdn.net/account_images/default_profile.jpeg")
                .build();
        userService.saveUser(user);
    }

    private void updateUser(AppleInfo appleUser) {
        User user = userService.findByUserId(Long.valueOf(appleUser.getSub()));
        userService.saveUser(user);
    }

    private Long convertSubToLong(String sub) {
        return Math.abs((long) sub.hashCode());
    }
}
