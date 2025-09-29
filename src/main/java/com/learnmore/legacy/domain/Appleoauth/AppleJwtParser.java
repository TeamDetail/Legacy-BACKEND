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

    public AppleInfo parseIdentityToken(String idToken) {
        try {
            // 1. Apple 공개키 가져오기
            Map appleKeys = webClient.get()
                    .uri("/auth/keys")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (appleKeys == null) throw new RuntimeException("Apple JWKS 조회 실패");

            // 2. 첫 번째 키 가져오기 (실무에서는 kid 비교 후 찾아야 함)
            List<Map<String, Object>> keys = (List<Map<String, Object>>) appleKeys.get("keys");
            Map<String, Object> key = keys.getFirst();

            // 3. 공개키 생성
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

            String sub = claims.getSubject(); // Apple 고유 ID
            String email = claims.get("email", String.class);

            // fullName은 Apple 최초 로그인 시만 제공, JWT에는 없음
            // 필요하면 클라이언트에서 전달받아 AppleInfo에 세팅
            return new AppleInfo(sub, email, null);

        } catch (Exception ex) {
            throw new RuntimeException("Apple JWT 파싱 실패: " + ex.getMessage(), ex);
        }
    }
}
