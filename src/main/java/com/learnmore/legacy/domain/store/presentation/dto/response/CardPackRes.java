package com.learnmore.legacy.domain.store.presentation.dto.response;

import com.learnmore.legacy.domain.store.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardPackRes {
    private List<CardPackDataRes> cardpack;
    private Integer buyCount;

    public static CardPackRes from(List<Store> cardpacks, Integer buyCount) {
        List<CardPackDataRes> cardpackResList = cardpacks.stream()
                .map(CardPackDataRes::from)
                .collect(Collectors.toList());

        return CardPackRes.builder()
                .cardpack(cardpackResList)
                .buyCount(buyCount)
                .build();
    }
}
