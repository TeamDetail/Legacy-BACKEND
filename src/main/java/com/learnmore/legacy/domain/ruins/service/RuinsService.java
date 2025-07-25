package com.learnmore.legacy.domain.ruins.service;

import com.learnmore.legacy.domain.card.error.CardError;
import com.learnmore.legacy.domain.card.model.Card;
import com.learnmore.legacy.domain.card.model.CardHistory;
import com.learnmore.legacy.domain.card.model.repo.CardHistoryJpaRepo;
import com.learnmore.legacy.domain.card.model.repo.CardJpaRepo;
import com.learnmore.legacy.domain.card.presentation.dto.response.CardRes;
import com.learnmore.legacy.domain.ruins.error.RuinsError;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import com.learnmore.legacy.domain.ruins.model.repo.RuinsJpaRepo;
import com.learnmore.legacy.domain.ruins.presentation.dto.response.RuinsDetailRes;
import com.learnmore.legacy.domain.ruins.presentation.dto.response.RuinsMapPointRes;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RuinsService {
    private final RuinsJpaRepo ruinsJpaRepo;
    private final CardJpaRepo cardJpaRepo;
    private final CardHistoryJpaRepo cardHistoryJpaRepo;

    public List<RuinsMapPointRes> getRuinsMapPoint(BigDecimal minLat, BigDecimal maxLat, BigDecimal minLng, BigDecimal maxLng) {
        return ruinsJpaRepo.findInBounds(minLat, maxLat, minLng, maxLng).stream()
                .map(RuinsMapPointRes::from)
                .toList();
    }

    public RuinsDetailRes getRuinsDetail(Long ruinsId) {
        Ruins ruins = ruinsJpaRepo.findById(ruinsId)
                .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

        List<Card> cards = cardJpaRepo.findAllByRuins_RuinsId(ruinsId);

        List<CardRes> cardResList = cards.stream()
                .map(card -> {
                    CardHistory history = cardHistoryJpaRepo.findTopByCard_CardId(card.getCardId())
                            .orElseThrow(() -> new CustomException(CardError.CARD_HISTORY_ERROR));
                    return CardRes.from(card, history);
                })
                .toList();

        return RuinsDetailRes.from(ruins, cardResList);
    }

    public RuinsDetailRes getRuinsDetailByRuinsName(String ruinsName) {
        Ruins ruins = ruinsJpaRepo.findByName(ruinsName)
                .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));
        System.out.println(ruins);

        List<Card> cards = cardJpaRepo.findAllByRuins_RuinsId(ruins.getRuinsId());

        List<CardRes> cardResList = cards.stream()
                .map(card -> {
                    CardHistory history = cardHistoryJpaRepo.findTopByCard_CardId(card.getCardId())
                            .orElseThrow(() -> new CustomException(CardError.CARD_HISTORY_ERROR));
                    return CardRes.from(card, history);
                })
                .toList();

        return RuinsDetailRes.from(ruins, cardResList);
    }
}
