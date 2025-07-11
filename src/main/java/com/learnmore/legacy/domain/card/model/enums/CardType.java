package com.learnmore.legacy.domain.card.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardType {
    START_CARD("START CARD"),
    BASIC_CARD("BASIC CARD"),
    SHINING_CARD("SHINING CARD");

    private final String cardType;
}
