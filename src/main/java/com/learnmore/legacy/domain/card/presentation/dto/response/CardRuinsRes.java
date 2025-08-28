package com.learnmore.legacy.domain.card.presentation.dto.response;

import com.learnmore.legacy.domain.card.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardRuinsRes {
    private Long cardId;
    private String cardName;
    private String cardImageUrl;
    private String nationAttributeName;
    private String lineAttributeName;
    private String regionAttributeName;

    public static CardRuinsRes from(Card card) {
        return CardRuinsRes.builder()
                .cardId(card.getCardId())
                .cardName(card.getCardName())
                .cardImageUrl(card.getCardImageUrl())
                .nationAttributeName(card.getNationAttribute().getAttributeName())
                .lineAttributeName(card.getLineAttribute().getAttributeName())
                .regionAttributeName(card.getRegionAttribute().getAttributeName())
                .build();
    }
}
