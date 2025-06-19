package com.learnmore.legacy.domain.auth.dao;

import com.learnmore.legacy.global.security.jwt.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenDao {

    private final JwtProperties jwtProperties;
    private final StringRedisTemplate redisTemplate;
    private static final String KEY_HEADER="RT";

    public void saveToken(String id, String token) {
        redisTemplate.opsForValue().set(KEY_HEADER+ id, token, Duration.ofMillis(jwtProperties.getRefreshExp()));
    }

    public Optional<String> getToken(String id) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(KEY_HEADER + id));
    }


    public void removeToken(String id) {
        redisTemplate.delete(KEY_HEADER+ id);
    }

    public boolean hasToken(String id) {
        return redisTemplate.hasKey(KEY_HEADER+ id);
    }

}
