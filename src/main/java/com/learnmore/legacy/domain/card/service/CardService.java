package com.learnmore.legacy.domain.card.service;

import com.learnmore.legacy.domain.card.model.*;
import com.learnmore.legacy.domain.card.model.enums.CardType;
import com.learnmore.legacy.domain.card.model.repo.*;
import com.learnmore.legacy.domain.card.presentation.dto.request.CardReq;
import com.learnmore.legacy.domain.card.presentation.dto.request.LineAttributeReq;
import com.learnmore.legacy.domain.card.presentation.dto.request.NationAttributeReq;
import com.learnmore.legacy.domain.card.presentation.dto.request.RegionAttributeReq;
import com.learnmore.legacy.domain.card.presentation.dto.response.CardRes;
import com.learnmore.legacy.domain.card.presentation.dto.response.LineAttributeRes;
import com.learnmore.legacy.domain.card.presentation.dto.response.NationAttributeRes;
import com.learnmore.legacy.domain.card.presentation.dto.response.RegionAttributeRes;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardJpaRepo cardJpaRepo;
    private final CardHistoryJpaRepo cardHistoryJpaRepo;
    private final DeckJpaRepo deckJpaRepo;
    private final NationAttributeJpaRepo nationAttributeJpaRepo;
    private final LineAttributeJpaRepo lineAttributeJpaRepo;
    private final RegionAttributeJpaRepo regionAttributeJpaRepo;
    private final UserJpaRepo userJpaRepo;

    public List<CardRes> getCardByCardId(Long userId) {
        List<CardHistory> histories = cardHistoryJpaRepo.findAllByUser_UserId(userId);
        return histories.stream()
                .map(history -> CardRes.from(history.getCard(), history))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countCardByUserId(Long userId) {
        return cardHistoryJpaRepo.countByUser_UserId(userId);
    }

    @Transactional(readOnly = true)
    public long countShiningCardByUserId(Long userId) {
        return cardHistoryJpaRepo.countByUser_UserIdAndCardType(userId, CardType.SHINING_CARD);
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

    @Transactional
    public CardRes addCard(CardReq cardReq) {
        NationAttribute nation = nationAttributeJpaRepo.findByAttributeName(cardReq.getNationAttributeName())
                .orElseThrow(() -> new EntityNotFoundException("국가 속성을 찾을 수 없습니다."));

        LineAttribute line = lineAttributeJpaRepo.findByAttributeName(cardReq.getLineAttributeName())
                .orElseThrow(() -> new EntityNotFoundException("개열 속성을 찾을 수 없습니다."));

        RegionAttribute region = regionAttributeJpaRepo.findByAttributeName(cardReq.getRegionAttributeName())
                .orElseThrow(() -> new EntityNotFoundException("지역 속성을 찾을 수 없습니다."));

        Card card = Card.builder()
                .cardName(cardReq.getCardName())
                .cardImageUrl(cardReq.getCardImageUrl())
                .nationAttribute(nation)
                .lineAttribute(line)
                .regionAttribute(region)
                .build();
        cardJpaRepo.save(card);

        User user = userJpaRepo.findByUserId(cardReq.getUserId());

        Deck deck = deckJpaRepo.findById(cardReq.getDeckId())
                .orElseThrow(() -> new EntityNotFoundException(""));

        CardHistory cardHistory = CardHistory.builder()
                .card(card)
                .user(user)
                .deck(deck)
                .cardType(cardReq.getCardType())
                .build();

        cardHistoryJpaRepo.save(cardHistory);
        return CardRes.from(card, cardHistory);
    }

    public NationAttributeRes addNation(NationAttributeReq nationAttributeReq) {
        NationAttribute nation = NationAttribute.builder()
                .attributeName(nationAttributeReq.getAttributeName())
                .build();
        nationAttributeJpaRepo.save(nation);
        return NationAttributeRes.from(nation);
    }

    public LineAttributeRes addLine(LineAttributeReq lineAttributeReq) {
        LineAttribute line = LineAttribute.builder()
                .attributeName(lineAttributeReq.getAttributeName())
                .build();
        lineAttributeJpaRepo.save(line);
        return LineAttributeRes.from(line);
    }

    public RegionAttributeRes addRegion(RegionAttributeReq regionAttributeReq) {
        RegionAttribute region = RegionAttribute.builder()
                .attributeName(regionAttributeReq.getAttributeName())
                .build();
        regionAttributeJpaRepo.save(region);
        return RegionAttributeRes.from(region);
    }
}
