package com.learnmore.legacy.domain.Appleoauth;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;

public class AppleJwtVerifier {

    private static final String APPLE_JWKS_URL = "https://appleid.apple.com/auth/keys";

    // 검증 메서드
    public static JWTClaimsSet verify(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);

        // 1. kid 가져오기
        String kid = signedJWT.getHeader().getKeyID();

        // 2. Apple 공개키 가져오기
        WebClient webClient = WebClient.create();
        Map<String, Object> response = webClient.get()
                .uri(APPLE_JWKS_URL)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("keys")) {
            throw new RuntimeException("Apple JWKS 조회 실패");
        }

        List<Map<String, Object>> keys = (List<Map<String, Object>>) response.get("keys");

        // 3. kid와 매칭되는 키 찾기
        Map<String, Object> keyMap = keys.stream()
                .filter(k -> kid.equals(k.get("kid")))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Apple 공개키(KID) 미발견"));

        JWK jwk = JWK.parse(keyMap);
        RSAPublicKey publicKey = (RSAPublicKey) jwk.toRSAKey().toPublicKey();

        // 4. 서명 검증
        JWSVerifier verifier = new RSASSAVerifier(publicKey);
        if (!signedJWT.verify(verifier)) {
            throw new RuntimeException("Apple JWT 서명 검증 실패");
        }

        // 5. Claims 추출
        return signedJWT.getJWTClaimsSet();
    }
}
