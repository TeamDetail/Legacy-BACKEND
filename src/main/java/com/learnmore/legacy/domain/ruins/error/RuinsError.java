package com.learnmore.legacy.domain.ruins.error;

import com.learnmore.legacy.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RuinsError implements CustomError {

    RUINS_NOT_FOUND(HttpStatus.NOT_FOUND, "유적지를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
