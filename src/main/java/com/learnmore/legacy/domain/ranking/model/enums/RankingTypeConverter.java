package com.learnmore.legacy.domain.ranking.model.enums;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RankingTypeConverter implements Converter<String, RankingType> {
    @Override
    public RankingType convert(String source) {
        for (RankingType type : RankingType.values()) {
            if (type.getRankingType().equalsIgnoreCase(source)) { // 대소문자 무시
                return type;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 RankingType: " + source);
    }
}