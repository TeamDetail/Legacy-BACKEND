package com.learnmore.legacy.domain.Googleoauth.service;

import com.learnmore.legacy.domain.Googleoauth.GoogleTokenVerifier;
import com.learnmore.legacy.domain.Googleoauth.error.GoogleAuthError;
import com.learnmore.legacy.domain.Googleoauth.presentation.dto.request.GoogleCodeReq;
import com.learnmore.legacy.domain.Googleoauth.presentation.dto.request.GoogleIdTokenReq;
import com.learnmore.legacy.domain.Googleoauth.presentation.dto.response.GoogleTokenResponse;
import com.learnmore.legacy.domain.Googleoauth.presentation.dto.response.GoogleUserInfo;
import com.learnmore.legacy.domain.auth.presentation.dto.response.TokenRes;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.enums.UserRole;
import com.learnmore.legacy.domain.user.service.UserService;
import com.learnmore.legacy.global.exception.CustomException;
import com.learnmore.legacy.global.properties.GoogleProperties;
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
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class GoogleService {

    private final GoogleProperties googleProperties;
    private final GoogleTokenVerifier tokenVerifier;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final WebClient webClient = WebClient.create("https://oauth2.googleapis.com");

    @Transactional
    public TokenRes loginWithWeb(GoogleCodeReq request) {
        // 1. authorization code로 토큰 교환
        GoogleTokenResponse tokenResponse = exchangeCodeForToken(request.code());

        // 2. ID 토큰 검증
        GoogleUserInfo userInfo = tokenVerifier.verifyIdToken(
                tokenResponse.getIdToken(),
                googleProperties.getWebClientId()
        );

        // 3. 사용자 처리 및 JWT 발급
        Long userId = processUser(userInfo);
        return jwtProvider.generateToken(userId.toString());
    }

    @Transactional
    public TokenRes loginWithAndroid(GoogleIdTokenReq request) {
        // 1. ID 토큰 검증
        GoogleUserInfo userInfo = tokenVerifier.verifyIdToken(
                request.idToken(),
                googleProperties.getAndroidClientId()
        );

        // 2. 사용자 처리 및 JWT 발급
        Long userId = processUser(userInfo);
        return jwtProvider.generateToken(userId.toString());
    }

    @Transactional
    public TokenRes loginWithIos(GoogleIdTokenReq request) {
        // 1. ID 토큰 검증
        GoogleUserInfo userInfo = tokenVerifier.verifyIdToken(
                request.idToken(),
                googleProperties.getIosClientId()
        );

        // 2. 사용자 처리 및 JWT 발급
        Long userId = processUser(userInfo);
        return jwtProvider.generateToken(userId.toString());
    }

    private GoogleTokenResponse exchangeCodeForToken(String code) {
        return webClient.post()
                .uri("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("grant_type", "authorization_code")
                        .with("client_id", googleProperties.getWebClientId())
                        .with("client_secret", googleProperties.getWebClientSecret())
                        .with("code", code)
                        .with("redirect_uri", googleProperties.getRedirectUri()))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(
                                        new CustomException(GoogleAuthError.TOKEN_REQUEST_FAILED, errorBody)
                                ))
                )
                .bodyToMono(GoogleTokenResponse.class)
                .block(Duration.ofSeconds(5));
    }

    private Long processUser(GoogleUserInfo googleUser) {
        Long userId = convertGoogleSubToUserId(googleUser.getSub());

        if (userService.existsByUserId(userId)) {
            updateExistingUser(userId, googleUser);
        } else {
            createNewUser(userId, googleUser);
        }

        return userId;
    }

    private void createNewUser(Long userId, GoogleUserInfo googleUser) {
        String nickname = googleUser.getName() != null
                ? googleUser.getName()
                : "Google 사용자";

        String imageUrl = googleUser.getPicture() != null
                ? googleUser.getPicture()
                : "http://img1.kakaocdn.net/thumb/R640x640.q70/?fname=http://t1.kakaocdn.net/account_images/default_profile.jpeg";

        User user = User.builder()
                .userId(userId)
                .nickname(nickname)
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
                .imageUrl(imageUrl)
                .build();

        userService.saveUser(user);
    }

    private void updateExistingUser(Long userId, GoogleUserInfo googleUser) {
        User user = userService.findByUserId(userId);

        boolean updated = false;

        if (googleUser.getName() != null &&
                !googleUser.getName().equals(user.getNickname())) {
            user.updateNickname(googleUser.getName());
            updated = true;
        }

        if (googleUser.getPicture() != null &&
                !googleUser.getPicture().equals(user.getImageUrl())) {
            user.updateImageUrl(googleUser.getPicture());
            updated = true;
        }

        if (updated) {
            userService.saveUser(user);
        }
    }

    private Long convertGoogleSubToUserId(String googleSub) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(googleSub.getBytes(StandardCharsets.UTF_8));

            BigInteger bigInt = new BigInteger(1, hash);
            return bigInt.mod(BigInteger.valueOf(Long.MAX_VALUE)).longValue();

        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(GoogleAuthError.JDK_ERROR);
        }
    }
}