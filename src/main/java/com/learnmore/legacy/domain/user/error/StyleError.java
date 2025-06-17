package com.learnmore.legacy.domain.user.error;

import com.learnmore.legacy.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StyleError implements CustomError {
    STYLE_NOT_FOUND(HttpStatus.NOT_FOUND, "칭호를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
