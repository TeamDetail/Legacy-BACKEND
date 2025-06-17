package com.learnmore.legacy.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalError implements CustomError {
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "사용할 수 없는 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다. (메시지: %s)"),
    HTTP_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "형식이 잘못되었거나 읽을 수 없는 요청입니다."),
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "메소드 인자 타입이 맞지 않습니다."),
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "검증되지 않은 메소드 인자입니다.");

    private final HttpStatus status;
    private final String message;
}