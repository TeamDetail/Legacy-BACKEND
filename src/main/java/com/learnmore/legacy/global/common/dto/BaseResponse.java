package com.learnmore.legacy.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
@Builder
public class BaseResponse<T> {

    private final T data;
    private final int status;

    public static <T> ResponseEntity<BaseResponse<T>> of(T data) {
        return of(data, HttpStatus.OK);
    }

    public static <T> ResponseEntity<BaseResponse<T>> of(T data, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(BaseResponse.<T>builder()
                        .data(data)
                        .status(status.value())
                        .build());
    }
}

