package com.learnmore.legacy.domain.Googleoauth.error;

import com.learnmore.legacy.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GoogleAuthError implements CustomError {
    TOKEN_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "구글 토큰 요청 실패 "),
    JDK_ERROR(HttpStatus.BAD_REQUEST, "JVM 환경을 확인하세요.");

    private final HttpStatus status;
    private final String message;
}