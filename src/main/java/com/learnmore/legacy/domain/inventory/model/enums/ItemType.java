package com.learnmore.legacy.domain.inventory.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemType {
    CARD_PACK("CARD PACK");

    private final String itemType;
}
