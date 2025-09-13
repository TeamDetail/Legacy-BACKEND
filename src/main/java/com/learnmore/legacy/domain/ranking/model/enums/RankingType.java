package com.learnmore.legacy.domain.ranking.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RankingType {
    ALL("ALL"),
    FRIEND("FRIEND");

    private final String rankingType;
}
