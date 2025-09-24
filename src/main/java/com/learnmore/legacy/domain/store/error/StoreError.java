package com.learnmore.legacy.domain.store.error;

import com.learnmore.legacy.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StoreError implements CustomError {
    STORE_ERROR(HttpStatus.NOT_FOUND, "상점을 찾을 수 없습니다."),
    CREDIT_ERROR(HttpStatus.NOT_FOUND, "크레딧이 부족합니다."),
    NOT_ENOUGH_ITEM(HttpStatus.NOT_FOUND, "아이템이 부족합니다.");

    private final HttpStatus status;
    private final String message;
}
