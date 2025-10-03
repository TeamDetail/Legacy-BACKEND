package com.learnmore.legacy.domain.Appleoauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class AppleJwtVerifier {

    public static JWTClaimsSet verify(String idToken) throws Exception {
        // 1. idToken을 .으로 분리
        String[] parts = idToken.split("\\.");
        if (parts.length != 3) throw new Exception("잘못된 Apple idToken");

        // 2. payload 디코딩
        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);

        // 3. JSON -> Map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> claimsMap = mapper.readValue(payloadJson, Map.class);

        // 4. 필수 검증
        if (!"https://appleid.apple.com".equals(claimsMap.get("iss"))) {
            throw new Exception("Apple issuer 검증 실패");
        }

        // 5. JWTClaimsSet 생성
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        claimsMap.forEach(builder::claim);
        return builder.build();
    }
}