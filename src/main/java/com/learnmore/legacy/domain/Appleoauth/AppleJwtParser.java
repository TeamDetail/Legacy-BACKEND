package com.learnmore.legacy.domain.Appleoauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnmore.legacy.domain.Appleoauth.presentation.dto.response.AppleInfo;
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
public class AppleJwtParser {

    private final WebClient webClient = WebClient.create("https://appleid.apple.com");
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AppleInfo parseIdentityToken(String idToken, String fullName) {
        try {
            // 1. Apple 공개키 가져오기
            Map<String, Object> appleKeys = webClient.get()
                    .uri("/auth/keys")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (appleKeys == null || !appleKeys.containsKey("keys")) {
                throw new RuntimeException("Apple JWKS 조회 실패");
            }

            List<Map<String, Object>> keys = (List<Map<String, Object>>) appleKeys.get("keys");

            // 2. JWT 헤더에서 kid 추출
            String[] tokenParts = idToken.split("\\.");
            if (tokenParts.length < 2) throw new RuntimeException("잘못된 idToken 형식");
            Map<String, Object> header = objectMapper.readValue(
                    new String(Base64.getUrlDecoder().decode(tokenParts[0])),
                    Map.class
            );
            String kid = (String) header.get("kid");

            // 3. kid에 맞는 공개키 선택
            Map<String, Object> key = keys.stream()
                    .filter(k -> kid.equals(k.get("kid")))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Apple 공개키 없음"));

            String n = (String) key.get("n");
            String e = (String) key.get("e");

            BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
            BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);

            // 4. JWT 파싱
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(idToken)
                    .getBody();

            String sub = claims.getSubject();
            String email = claims.get("email", String.class);

            return new AppleInfo(sub, email, fullName);

        } catch (Exception ex) {
            throw new RuntimeException("Apple JWT 파싱 실패: " + ex.getMessage(), ex);
        }
    }
}