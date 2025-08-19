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

    public CardsRes getCardsByRegion(String region) {
        List<Card> cards = cardJpaRepo.findAllByRegionAttribute_AttributeName(region);

        List<CardRes> cardResList = cards.stream()
                .map(card -> {
                    CardHistory cardType = cardHistoryJpaRepo
                            .findTopByCard_CardIdOrderByHistoryIdDesc(card.getCardId())
                            .orElseThrow(() -> new CustomException(CardError.CARD_HISTORY_ERROR));

                    return CardRes.from(card, cardType);
                })
                .toList();

        return CardsRes.builder()
                .maxCount((long) cardResList.size())
                .cards(cardResList)
                .build();
    }

    @Transactional
    public CardRes addCard(CardReq cardReq) {
        NationAttribute nation = nationAttributeJpaRepo.findByAttributeName(cardReq.getNationAttributeName())
                .orElseThrow(() -> new CustomException(CardError.NATION_ATTRIBUTE_NOT_FOUND));

        LineAttribute line = lineAttributeJpaRepo.findByAttributeName(cardReq.getLineAttributeName())
                .orElseThrow(() -> new CustomException(CardError.LINE_ATTRIBUTE_ERROR));

        RegionAttribute region = regionAttributeJpaRepo.findByAttributeName(cardReq.getRegionAttributeName())
                .orElseThrow(() -> new CustomException(CardError.REGION_ATTRIBUTE_ERROR));

        QuizHistory quizHistory = quizHistoryJpaRepo.findById(cardReq.getQuizHistoryId())
                .orElseThrow(() -> new CustomException(QuizError.QUIZ_NOT_FOUND));

        Ruins ruins = ruinsJpaRepo.findByNameContaining(cardReq.getCardName())
                .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

        Card card = Card.builder()
                .cardName(cardReq.getCardName())
                .cardImageUrl(cardReq.getCardImageUrl())
                .ruins(ruins)
                .nationAttribute(nation)
                .quizHistory(quizHistory)
                .lineAttribute(line)
                .regionAttribute(region)
                .build();
        cardJpaRepo.save(card);

        User user = userJpaRepo.findByUserId(cardReq.getUserId());

        Deck deck = deckJpaRepo.findById(cardReq.getDeckId())
                .orElseThrow(() -> new EntityNotFoundException(CardError.DECK_ERROR.getMessage()));

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
