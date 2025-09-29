package com.learnmore.legacy.global.properties;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "apple.auth")
public class AppleProperties {

    // getters & setters (lombok @Data 제거 권장)
    private String clientId;
    private String teamId;
    private String keyId;
    private String redirectUri;
    private String privateKeyPath;

    public String generateClientSecret() {
        try {
            Instant now = Instant.now();
            Instant exp = now.plusSeconds(60 * 5);

            return Jwts.builder()
                    .setHeaderParam("kid", keyId)
                    .setHeaderParam("alg", "ES256")
                    .setIssuer(teamId)
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(exp))
                    .setAudience("https://appleid.apple.com")
                    .setSubject(clientId)
                    .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Apple client_secret 생성 실패", e);
        }
    }

    private PrivateKey getPrivateKey() throws Exception {
        // privateKeyPath에서 파일 읽기
        try (InputStream is = getClass().getResourceAsStream(privateKeyPath.replace("classpath:", "/"))) {
            if (is == null) {
                throw new IllegalArgumentException("p8 파일을 찾을 수 없음: " + privateKeyPath);
            }
            String key = new String(is.readAllBytes())
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] keyBytes = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("EC").generatePrivate(keySpec);
        }
    }

}
