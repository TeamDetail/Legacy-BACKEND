package com.learnmore.legacy.domain.card.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DeckError {
    DECK_ERROR(HttpStatus.NOT_FOUND, "덱 아이디를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
