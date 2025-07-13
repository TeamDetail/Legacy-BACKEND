package com.learnmore.legacy.domain.card.presentation.dto.response;

import com.learnmore.legacy.domain.card.model.*;
import com.learnmore.legacy.domain.card.model.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardRes {
    private Long cardId;
    private String cardName;
    private String cardImageUrl;
    private CardType cardType;
    private String nationAttributeName;
    private String lineAttributeName;
    private String regionAttributeName;

    public static CardRes from(Card card, CardHistory cardHistory) {
        return CardRes.builder()
                .cardId(card.getCardId())
                .cardName(card.getCardName())
                .cardImageUrl(card.getCardImageUrl())
                .cardType(cardHistory.getCardType())
                .nationAttributeName(card.getNationAttribute().getAttributeName())
                .lineAttributeName(card.getLineAttribute().getAttributeName())
                .regionAttributeName(card.getRegionAttribute().getAttributeName())
                .build();
    }
}
