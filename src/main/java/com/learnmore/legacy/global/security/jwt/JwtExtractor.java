package com.learnmore.legacy.global.security.jwt;

import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import com.learnmore.legacy.domain.Appleoauth.AppleJwtVerifier;
import com.learnmore.legacy.global.exception.CustomException;
import com.learnmore.legacy.global.security.auth.AuthDetails;
import com.learnmore.legacy.global.security.jwt.config.JwtProperties;
import com.learnmore.legacy.global.security.jwt.enums.JwtType;
import com.learnmore.legacy.global.security.jwt.error.JwtError;
import com.nimbusds.jwt.JWTClaimsSet;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
@RequiredArgsConstructor
public class JwtExtractor {

    private final JwtProperties jwtProperties;
    private final UserJpaRepo userJpaRepo;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getId(String token) {
        return getClaims(token).getBody().getSubject();
    }

    public Authentication getAuthentication(String token) {
        if (isAppleToken(token)) {
            try {
                // Apple 서버에서 토큰 검증
                JWTClaimsSet claims = AppleJwtVerifier.verify(token);
                String appleSub = claims.getSubject(); // Apple sub (문자열)

                // 안전하게 Long 변환
                Long appleUserId = convertSubToLong(appleSub);

                User user = userJpaRepo.findByUserId(appleUserId);

                AuthDetails details = new AuthDetails(user);
                return new UsernamePasswordAuthenticationToken(
                        details,
                        null,
                        details.getAuthorities()
                );
            } catch (Exception e) {
                throw new CustomException(JwtError.MALFORMED_TOKEN, "Apple JWT 처리 실패: " + e.getMessage());
            }
        } else {
            Claims claims = getClaims(token).getBody();
            User user = userJpaRepo.findByUserId(Long.valueOf(claims.getSubject()));

            AuthDetails details = new AuthDetails(user);
            return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
        }
    }


    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader(jwtProperties.getHeader());
        if (header != null && header.startsWith(jwtProperties.getPrefix())) {
            return header.substring(jwtProperties.getPrefix().length()).trim();
        }
        return null;
    }


    private Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new CustomException(JwtError.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(JwtError.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(JwtError.INVALID_TOKEN);
        } catch (MalformedJwtException e) {
            throw new CustomException(JwtError.MALFORMED_TOKEN);
        }
    }

    public boolean validateTokenType(String token, JwtType type) {
        Jws<Claims> claims = getClaims(token);

        return claims.getHeader().equals(type);
    }

    private boolean isAppleToken(String token) {
        if (token == null || token.isBlank()) return false;
        // Apple JWT는 header.payload.signature 구조를 가지고 있음
        String[] parts = token.split("\\.");
        return parts.length == 3;
    }

    private Long convertSubToLong(String appleSub) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(appleSub.getBytes(StandardCharsets.UTF_8));

            // 앞 8바이트를 long으로 변환
            ByteBuffer buffer = ByteBuffer.wrap(digest);
            long value = buffer.getLong();
            return value & 0x7FFFFFFFFFFFFFFFL; // 양수로 변환
        } catch (Exception e) {
            throw new RuntimeException("Apple Sub 변환 실패", e);
        }
    }
}