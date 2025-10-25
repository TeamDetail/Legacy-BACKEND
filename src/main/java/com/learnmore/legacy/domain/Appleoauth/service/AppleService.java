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

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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
        AppleInfo userInfo = appleJwtParser.parseIdentityToken(token.getIdToken(), null);

        // 사용자 upsert
        upsertUser(userInfo);

        // JWT 발급
        return jwtProvider.generateToken(userInfo.getSub());
    }

    @Transactional
    public TokenRes loginAppleApp(AppleIdTokenReq req) {
        AppleInfo userInfo = appleJwtParser.parseIdentityToken(req.idToken(), null);

        upsertUser(userInfo);

        Long userId = convertSubToLong(userInfo.getSub());
        return jwtProvider.generateToken(userId.toString());
    }

    @Transactional
    public void updateNickname(Long userId, String nickname) {
        User user = userService.findByUserId(userId);
        user.updateNickname(nickname);
        userService.saveUser(user);
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
        } else {
            saveUser(userInfo);
        }
    }

    private void saveUser(AppleInfo appleUser) {
        Long userId = convertSubToLong(appleUser.getSub());
        String nickname = "";

        User user = User.builder()
                .userId(userId)
                .nickname(nickname)
                .description("")
                .level(1)
                .exp(0)
                .credit(10000000)
                .snowflakeCapacity(5)
                .storeRestock(1)
                .creditCollect(3)
                .dropCount(1)
                .role(UserRole.USER)
                .allBlocks(0)
                .ruinsBlocks(0)
                .maxFloor(0)
                .maxScore(0)
                .imageUrl("http://img1.kakaocdn.net/thumb/R640x640.q70/?fname=http://t1.kakaocdn.net/account_images/default_profile.jpeg")
                .build();
        userService.saveUser(user);
    }



    private Long convertSubToLong(String appleSub) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(appleSub.getBytes(StandardCharsets.UTF_8));

            BigInteger bigInt = new BigInteger(1, digest); // 1 = 양수 처리

            // 앞 63비트만 사용 (Long의 양수 범위)
            return bigInt.mod(BigInteger.valueOf(Long.MAX_VALUE)).longValue();

        } catch (Exception e) {
            throw new RuntimeException("Apple Sub 변환 실패", e);
        }
    }
}
