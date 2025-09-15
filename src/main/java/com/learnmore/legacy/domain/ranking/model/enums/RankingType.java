package com.learnmore.legacy.domain.ranking.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RankingType {
    ALL("all"),
    FRIEND("friend");

    private final String rankingType;

    @JsonCreator
    public static RankingType from(String value) {
        for (RankingType type : values()) {
            if (type.rankingType.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ranking type: " + value);
    }
}
