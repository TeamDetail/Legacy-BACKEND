package com.learnmore.legacy.domain.Googleoauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnmore.legacy.domain.Googleoauth.presentation.dto.response.GoogleUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleTokenVerifier {

    private final WebClient webClient = WebClient.create("https://www.googleapis.com");
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GoogleUserInfo verifyIdToken(String idToken, String expectedAudience) {
        try {
            // 1. Google 공개키 조회
            Map<String, Object> jwks = fetchGooglePublicKeys();
            List<Map<String, Object>> keys = (List<Map<String, Object>>) jwks.get("keys");

            // 2. JWT 헤더 파싱
            String[] parts = idToken.split("\\.");
            if (parts.length != 3) {
                throw new RuntimeException("Invalid ID token format");
            }

            Map<String, Object> header = objectMapper.readValue(
                    Base64.getUrlDecoder().decode(parts[0]),
                    Map.class
            );
            String kid = (String) header.get("kid");

            // 3. kid로 공개키 찾기
            Map<String, Object> jwk = keys.stream()
                    .filter(k -> kid.equals(k.get("kid")))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Public key not found"));

            // 4. RSA 공개키 생성
            PublicKey publicKey = buildRSAPublicKey(jwk);

            // 5. JWT 검증 및 파싱
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(idToken)
                    .getBody();

            // 6. iss 검증
            String issuer = claims.getIssuer();
            if (!"https://accounts.google.com".equals(issuer) &&
                    !"accounts.google.com".equals(issuer)) {
                throw new RuntimeException("Invalid issuer: " + issuer);
            }

            // 7. aud 검증
            String audience = claims.get("aud", String.class);
            if (!expectedAudience.equals(audience)) {
                throw new RuntimeException("Invalid audience. Expected: " + expectedAudience + ", Got: " + audience);
            }

            // 8. 사용자 정보 추출
            return new GoogleUserInfo(
                    claims.getSubject(),
                    claims.get("email", String.class),
                    claims.get("name", String.class),
                    claims.get("picture", String.class),
                    claims.get("email_verified", Boolean.class)
            );

        } catch (Exception e) {
            throw new RuntimeException("ID token verification failed: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> fetchGooglePublicKeys() {
        Map<String, Object> response = webClient.get()
                .uri("/oauth2/v3/certs")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("keys")) {
            throw new RuntimeException("Failed to fetch Google public keys");
        }
        return response;
    }

    private PublicKey buildRSAPublicKey(Map<String, Object> jwk) throws Exception {
        String n = (String) jwk.get("n");
        String e = (String) jwk.get("e");

        BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
        BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));

        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }
}