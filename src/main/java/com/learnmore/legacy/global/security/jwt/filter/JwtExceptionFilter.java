package com.learnmore.legacy.global.security.jwt.filter;

import com.learnmore.legacy.global.exception.CustomException;
import com.learnmore.legacy.global.exception.ErrorResponse;
import com.learnmore.legacy.global.security.jwt.error.JwtError;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public JwtExceptionFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            sendError(response, new CustomException(JwtError.EXPIRED_TOKEN));
        } catch (MalformedJwtException e) {
            e.printStackTrace();
            sendError(response, new CustomException(JwtError.MALFORMED_TOKEN));
        } catch (CustomException e) {
            sendError(response, e);
        }
    }

    private void sendError(HttpServletResponse response, CustomException exception) throws IOException {
        var error = exception.getError();

        response.setStatus(error.getStatus().value());

        try (OutputStream outputStream = response.getOutputStream()) {
            outputStream.write(objectMapper.writeValueAsBytes(ErrorResponse.of(exception).getBody()));
            outputStream.flush();
        }
    }
}