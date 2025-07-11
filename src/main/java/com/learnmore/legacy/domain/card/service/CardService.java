package com.learnmore.legacy.domain.card.service;

import com.learnmore.legacy.domain.card.model.Card;
import com.learnmore.legacy.domain.card.model.CardHistory;
import com.learnmore.legacy.domain.card.model.repo.CardHistoryJpaRepo;
import com.learnmore.legacy.domain.card.model.repo.CardJpaRepo;
import com.learnmore.legacy.domain.card.presentation.dto.response.CardRes;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardJpaRepo cardJpaRepo;
    private final CardHistoryJpaRepo cardHistoryJpaRepo;


    public CardRes getCardByCardId(Long cardId) {
        Card card = cardJpaRepo.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("카드를 찾을 없습니다."));

        CardHistory cardType = cardHistoryJpaRepo.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("카드 타입을 찾을 수 없습니다."));

        return CardRes.from(card, cardType);
    }

    public List<CardRes> getCardsByRegion(String region) {
        List<Card> cards = cardJpaRepo.findAllByRegionAttribute_AttributeName(region);

        return cards.stream()
                .map(card -> {
                    CardHistory cardType = cardHistoryJpaRepo
                            .findTopByCard_CardIdOrderByHistoryIdDesc(card.getCardId())
                            .orElse(null);

                    return CardRes.from(card, cardType);
                })
                .toList();
    }
}
