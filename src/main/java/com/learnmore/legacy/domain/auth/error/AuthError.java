package com.learnmore.legacy.domain.auth.error;

import com.learnmore.legacy.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthError implements CustomError {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다.");

    private final HttpStatus status;
    private final String message;
}


