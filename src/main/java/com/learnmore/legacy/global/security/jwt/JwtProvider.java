package com.learnmore.legacy.global.security.jwt;



import com.learnmore.legacy.domain.auth.dao.RefreshTokenDao;
import com.learnmore.legacy.domain.auth.presentation.dto.response.TokenRes;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import com.learnmore.legacy.global.security.jwt.config.JwtProperties;
import com.learnmore.legacy.global.security.jwt.enums.JwtType;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final UserJpaRepo userRepository;
    private final RefreshTokenDao refreshTokenDao;

    public JwtProvider(JwtProperties jwtProperties, UserJpaRepo userRepository, RefreshTokenDao refreshTokenDao) {
        this.jwtProperties = jwtProperties;
        this.userRepository = userRepository;
        this.refreshTokenDao = refreshTokenDao;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenRes generateToken(String id) {
        String refreshToken=generateRefreshToken(id);
        refreshTokenDao.saveToken(id, refreshToken);
        return new TokenRes(
                generateAccessToken(id),
                refreshToken
        );
    }

    public String generateAccessToken(String id) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.JWT_TYPE, JwtType.ACCESS)
                .setSubject(id)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getAccessExp()))
                .signWith(getSigningKey())
                .compact();
    }

    private String generateRefreshToken(String id) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.JWT_TYPE, JwtType.REFRESH)
                .setSubject(id)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getRefreshExp()))
                .signWith(getSigningKey())
                .compact();
    }
}
