package com.learnmore.legacy.domain.Appleoauth;

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

    public AppleInfo parseIdentityToken(String idToken, String name) {
        try {
            // 1. Apple 공개키 가져오기
            Map appleKeys = webClient.get()
                    .uri("/auth/keys")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (appleKeys == null) throw new RuntimeException("Apple JWKS 조회 실패");

            List<Map<String, Object>> keys = (List<Map<String, Object>>) appleKeys.get("keys");

            // 2. JWT 헤더에서 kid 추출
            String kid = Jwts.parserBuilder()
                    .build()
                    .parseClaimsJws(idToken)
                    .getHeader()
                    .getKeyId();

            // 3. kid와 일치하는 공개키 선택
            Map<String, Object> key = keys.stream()
                    .filter(k -> kid.equals(k.get("kid")))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Apple JWT kid 불일치"));

            // 4. 공개키 생성
            String n = (String) key.get("n");
            String e = (String) key.get("e");
            BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
            BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);

            // 5. JWT 파싱
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(idToken)
                    .getBody();

            String sub = claims.getSubject();
            String email = claims.get("email", String.class);

            return new AppleInfo(sub, email, name != null ? name : "AppleUser");

        } catch (Exception ex) {
            throw new RuntimeException("Apple JWT 파싱 실패: " + ex.getMessage(), ex);
        }
    }
}