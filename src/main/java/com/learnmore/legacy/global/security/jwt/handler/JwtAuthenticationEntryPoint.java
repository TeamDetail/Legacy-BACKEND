package com.learnmore.legacy.global.security.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnmore.legacy.global.exception.CustomException;
import com.learnmore.legacy.global.exception.ErrorResponse;
import com.learnmore.legacy.global.security.jwt.error.JwtError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        CustomException customException = new CustomException(JwtError.TOKEN_NOT_FOUND);

        ErrorResponse errorResponse = new ErrorResponse(
                customException.getCode(),
                customException.getStatus(),
                customException.getMessage()
        );

        response.setStatus(customException.getStatus());
        response.setContentType("application/json;charset=UTF-8");

        String json = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);

    }
}