package com.learnmore.legacy.global.security.jwt;

import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import com.learnmore.legacy.domain.user.error.UserError;
import com.learnmore.legacy.global.exception.CustomException;
import com.learnmore.legacy.global.security.auth.AuthDetails;
import com.learnmore.legacy.global.security.jwt.config.JwtProperties;
import com.learnmore.legacy.global.security.jwt.enums.JwtType;
import com.learnmore.legacy.global.security.jwt.error.JwtError;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtExtractor {

    private final JwtProperties jwtProperties;
    private final UserJpaRepo userJpaRepo;

    public JwtExtractor(JwtProperties jwtProperties, UserJpaRepo userJpaRepo) {
        this.jwtProperties = jwtProperties;
        this.userJpaRepo = userJpaRepo;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getId(String token) {
        return getClaims(token).getBody().getSubject();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token).getBody();
        User user = userJpaRepo.findByUserId(Long.valueOf(claims.getSubject()));

        if (user == null) {
            throw new CustomException(UserError.USER_NOT_FOUND, claims.getSubject());
        }

        AuthDetails details = new AuthDetails(user);

        return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader(jwtProperties.getHeader());
        if (header != null && header.startsWith(jwtProperties.getPrefix())) {
            return header.substring(jwtProperties.getPrefix().length());
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
}