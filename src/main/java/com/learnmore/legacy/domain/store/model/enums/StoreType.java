package com.learnmore.legacy.domain.store.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreType {
    CARD_PACK("CARD PACK"),
    STYLE("STYLE");

    private final String storeType;

}
