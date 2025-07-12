package com.learnmore.legacy.domain.card.presentation.dto.request;

import com.learnmore.legacy.domain.card.model.enums.CardType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CardReq {
    private Long userId;
    private Long deckId;
    private String cardName;
    private String cardImageUrl;
    private CardType cardType;
    private String nationAttributeName;
    private String lineAttributeName;
    private String regionAttributeName;
}
