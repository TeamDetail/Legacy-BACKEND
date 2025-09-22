package com.learnmore.legacy.domain.inventory.error;

import com.learnmore.legacy.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InventoryError implements CustomError {
    ITEM_ERROR(HttpStatus.BAD_REQUEST, "아이템이 부족합니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "아이템을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
