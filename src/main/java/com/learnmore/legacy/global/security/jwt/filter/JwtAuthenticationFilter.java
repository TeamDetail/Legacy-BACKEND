package com.learnmore.legacy.global.security.jwt.filter;

import com.learnmore.legacy.global.security.jwt.JwtExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtExtractor jwtExtractor;

    public JwtAuthenticationFilter(JwtExtractor jwtExtractor) {
        this.jwtExtractor = jwtExtractor;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 이미 인증되어 있으면 스킵
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtExtractor.extractToken(request);

        if (token != null) {
            try {
                // JWT가 있으면 인증 처리
                SecurityContextHolder.getContext().setAuthentication(jwtExtractor.getAuthentication(token));
            } catch (Exception e) {
                // permitAll 경로라면 예외 무시하고 통과
                // 인증이 필요한 경로면 JwtExceptionFilter에서 처리됨
            }
        }

        filterChain.doFilter(request, response);
    }
}
