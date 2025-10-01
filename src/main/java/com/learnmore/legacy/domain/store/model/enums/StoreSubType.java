package com.learnmore.legacy.domain.store.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreSubType {
    REGION("REGION"),
    NATION("NATION"),
    LINE("LINE");

    private final String storeSubType;
}
