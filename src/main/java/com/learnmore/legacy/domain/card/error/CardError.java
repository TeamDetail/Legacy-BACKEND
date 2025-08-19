package com.learnmore.legacy.domain.card.error;

import com.learnmore.legacy.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CardError implements CustomError {
    CARD_HISTORY_ERROR(HttpStatus.NOT_FOUND, "카드 내역을 찾을 수 없습니다."),
    DECK_ERROR(HttpStatus.NOT_FOUND, "덱 아이디를 찾을 수 없습니다."),
    LINE_ATTRIBUTE_ERROR(HttpStatus.NOT_FOUND, "개열 속성을 찾을 수 없습니다."),
    NATION_ATTRIBUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "국가 속성을 찾을 수 없습니다."),
    REGION_ATTRIBUTE_ERROR(HttpStatus.NOT_FOUND, "지역 속성을 찾을 수 없습니다."),
    NOT_FOUND_INVENTORY(HttpStatus.NOT_FOUND, "아이템을 찾을 수 없습니다."),
    NOT_ENOUGH_ITEM(HttpStatus.NOT_FOUND, "아이템이 충분하지 않습니다."),;

    private final HttpStatus status;
    private final String message;

}
