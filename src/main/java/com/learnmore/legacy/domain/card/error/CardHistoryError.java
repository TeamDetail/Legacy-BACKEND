package com.learnmore.legacy.domain.card.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CardHistoryError {
    CARD_HISTORY_ERROR(HttpStatus.BAD_REQUEST, "Card history error");

    private final HttpStatus status;
    private final String message;
}
