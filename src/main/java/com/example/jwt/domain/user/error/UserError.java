package com.example.jwt.domain.user.error;

import com.example.jwt.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserError implements CustomError {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    ID_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 사용중인 id 입니다.");

    private final HttpStatus status;
    private final String message;
}
