package com.learnmore.legacy.domain.event.error;

import com.learnmore.legacy.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EventError implements CustomError {
    EVENT_ERROR(HttpStatus.NOT_FOUND, "이벤트를 찾을 수 없습니다."),
    EVENT_ALREADY(HttpStatus.ALREADY_REPORTED, "이미 이벤트를 완료했습니다.");


    private final HttpStatus status;
    private final String message;
}
