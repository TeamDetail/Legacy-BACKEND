package com.learnmore.legacy.domain.ruins.service;

import com.learnmore.legacy.domain.card.model.Card;
import com.learnmore.legacy.domain.card.model.CardHistory;
import com.learnmore.legacy.domain.card.model.repo.CardHistoryJpaRepo;
import com.learnmore.legacy.domain.card.model.repo.CardJpaRepo;
import com.learnmore.legacy.domain.card.presentation.dto.response.CardRes;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import com.learnmore.legacy.domain.ruins.model.repo.RuinsJpaRepo;
import com.learnmore.legacy.domain.ruins.presentation.dto.response.RuinsDetailRes;
import com.learnmore.legacy.domain.ruins.presentation.dto.response.RuinsMapPointRes;
import jakarta.persistence.EntityNotFoundException;
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
                .orElseThrow(() -> new EntityNotFoundException("유적지를 찾을 수 없습니다."));

        List<Card> cards = cardJpaRepo.findAllByRuins_RuinsId(ruinsId);

        List<CardRes> cardResList = cards.stream()
                .map(card -> {
                    CardHistory history = cardHistoryJpaRepo.findTopByCard_CardId(card.getCardId())
                            .orElseThrow(() -> new EntityNotFoundException("카드 기록을 찾을 수 없습니다."));
                    return CardRes.from(card, history);
                })
                .toList();

        return RuinsDetailRes.from(ruins, cardResList);
    }
}
