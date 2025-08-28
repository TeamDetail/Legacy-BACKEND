package com.learnmore.legacy.domain.card.service;

import com.learnmore.legacy.domain.card.error.*;
import com.learnmore.legacy.domain.card.model.*;
import com.learnmore.legacy.domain.card.model.enums.CardType;
import com.learnmore.legacy.domain.card.model.repo.*;
import com.learnmore.legacy.domain.card.presentation.dto.request.CardReq;
import com.learnmore.legacy.domain.card.presentation.dto.request.LineAttributeReq;
import com.learnmore.legacy.domain.card.presentation.dto.request.NationAttributeReq;
import com.learnmore.legacy.domain.card.presentation.dto.request.RegionAttributeReq;
import com.learnmore.legacy.domain.card.presentation.dto.response.*;
import com.learnmore.legacy.domain.quiz.error.QuizError;
import com.learnmore.legacy.domain.quiz.model.QuizHistory;
import com.learnmore.legacy.domain.quiz.model.repo.QuizHistoryJpaRepo;
import com.learnmore.legacy.domain.ruins.error.RuinsError;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import com.learnmore.legacy.domain.ruins.model.repo.RuinsJpaRepo;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import com.learnmore.legacy.global.exception.CustomException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {
    private final CardJpaRepo cardJpaRepo;
    private final CardHistoryJpaRepo cardHistoryJpaRepo;
    private final DeckJpaRepo deckJpaRepo;
    private final NationAttributeJpaRepo nationAttributeJpaRepo;
    private final LineAttributeJpaRepo lineAttributeJpaRepo;
    private final RegionAttributeJpaRepo regionAttributeJpaRepo;
    private final UserJpaRepo userJpaRepo;
    private final QuizHistoryJpaRepo quizHistoryJpaRepo;
    private final RuinsJpaRepo ruinsJpaRepo;

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

    public RegionRes getCardsByRegion(String region, Long userId) {
        List<Card> cards = cardHistoryJpaRepo.findCardsByUserIdAndRegion(userId, region);

        List<CardRes> cardResList = cards.stream()
                .map(card -> CardRes.builder()
                        .cardId(card.getCardId())
                        .cardName(card.getCardName())
                        .cardImageUrl(card.getCardImageUrl())
                        .nationAttributeName(card.getNationAttribute().getAttributeName())
                        .lineAttributeName(card.getLineAttribute().getAttributeName())
                        .regionAttributeName(card.getRegionAttribute().getAttributeName())
                        .build())
                .toList();

        return RegionRes.builder()
                .maxCount((long) cardResList.size())
                .cards(cardResList)
                .build();
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
