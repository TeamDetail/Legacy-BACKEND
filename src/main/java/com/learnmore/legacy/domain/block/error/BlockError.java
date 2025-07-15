package com.learnmore.legacy.domain.block.error;

import com.learnmore.legacy.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BlockError implements CustomError {

    BLOCK_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 위치에 이미 블록이 존재합니다.");

    private final HttpStatus status;
    private final String message;
}
