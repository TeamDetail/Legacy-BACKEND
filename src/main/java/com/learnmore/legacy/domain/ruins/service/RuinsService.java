package com.learnmore.legacy.domain.ruins.service;

import com.learnmore.legacy.domain.card.error.CardError;
import com.learnmore.legacy.domain.card.model.Card;
import com.learnmore.legacy.domain.card.model.repo.CardHistoryJpaRepo;
import com.learnmore.legacy.domain.card.model.repo.CardJpaRepo;
import com.learnmore.legacy.domain.card.presentation.dto.response.CardRuinsRes;
import com.learnmore.legacy.domain.ruins.error.RuinsError;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import com.learnmore.legacy.domain.ruins.model.RuinsComment;
import com.learnmore.legacy.domain.ruins.model.repo.RuinsCommentJpaRepo;
import com.learnmore.legacy.domain.ruins.model.repo.RuinsJpaRepo;
import com.learnmore.legacy.domain.ruins.presentation.dto.request.RuinsCommentReq;
import com.learnmore.legacy.domain.ruins.presentation.dto.response.RuinsCommentRes;
import com.learnmore.legacy.domain.ruins.presentation.dto.response.RuinsDetailRes;
import com.learnmore.legacy.domain.ruins.presentation.dto.response.RuinsMapPointRes;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RuinsService {
    private final RuinsJpaRepo ruinsJpaRepo;
    private final CardJpaRepo cardJpaRepo;
    private final CardHistoryJpaRepo cardHistoryJpaRepo;
    private final RuinsCommentJpaRepo ruinsCommentJpaRepo;

    public List<RuinsMapPointRes> getRuinsMapPoint(BigDecimal minLat, BigDecimal maxLat, BigDecimal minLng, BigDecimal maxLng) {
        return ruinsJpaRepo.findInBounds(minLat, maxLat, minLng, maxLng).stream()
                .map(RuinsMapPointRes::from)
                .toList();
    }

    // 카드 리스트 형식에서 단일 카드로 변경
    public RuinsDetailRes getRuinsDetail(Long ruinsId) {
        Ruins ruins = ruinsJpaRepo.findById(ruinsId)
                .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

        Card card = cardJpaRepo.findByRuins_RuinsId(ruinsId)
                .orElseThrow(() -> new CustomException(CardError.CARD_NOT_FOUND));

//        CardHistory history = cardHistoryJpaRepo.findTopByCard_CardId(card.getCardId())
//                .orElseThrow(() -> new CustomException(CardError.CARD_HISTORY_ERROR));

//        List<Card> cards = cardJpaRepo.findAllByRuins_RuinsId(ruinsId);

//        List<CardRes> cardResList = cards.stream()
//                .map(card -> {
//                    CardHistory history = cardHistoryJpaRepo.findTopByCard_CardId(card.getCardId())
//                            .orElseThrow(() -> new CustomException(CardError.CARD_HISTORY_ERROR));
//                    return CardRes.from(card, history);
//                })
//                .toList();

        return RuinsDetailRes.from(ruins, CardRuinsRes.from(card));
    }

    // 카드 리스트 형식에서 단일 카드로 변경

    public Ruins findNearestRuins (BigDecimal lat, BigDecimal lng) {
        return ruinsJpaRepo.findNearestRuins(lat,lng)
                .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));
    }

    public List<RuinsDetailRes> getRuinsDetailByRuinsName(String ruinsName) {
        List<Ruins> ruinsList = ruinsJpaRepo.searchByName(ruinsName);

        if (ruinsList.isEmpty()) {
            throw new CustomException(RuinsError.RUINS_NOT_FOUND);
        }

//        CardHistory history = cardHistoryJpaRepo.findTopByCard_CardId(card.getCardId())
//                .orElseThrow(() -> new CustomException(CardError.CARD_HISTORY_ERROR));

//        List<Card> cards = cardJpaRepo.findAllByRuins_RuinsId(ruins.getRuinsId());
//
//        List<CardRes> cardResList = cards.stream()
//                .map(card -> {
//                    CardHistory history = cardHistoryJpaRepo.findTopByCard_CardId(card.getCardId())
//                            .orElseThrow(() -> new CustomException(CardError.CARD_HISTORY_ERROR));
//                    return CardRes.from(card, history);
//                })
//                .toList();

        return ruinsList.stream()
                .map(ruins -> {
                    Card card = cardJpaRepo.findByRuins_RuinsId(ruins.getRuinsId())
                            .orElseThrow(() -> new CustomException(CardError.CARD_NOT_FOUND));

                    return RuinsDetailRes.from(ruins, CardRuinsRes.from(card));
                })
                .toList();
    }

    @Transactional
    public RuinsCommentRes addRuinsComment(RuinsCommentReq ruinsCommentReq, String userName, String userImgUrl) {
        Ruins ruins = ruinsJpaRepo.findById(ruinsCommentReq.ruinsId())
                .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

        RuinsComment comment = RuinsComment.builder()
                .userName(userName)
                .userImgUrl(userImgUrl)
                .ruins(ruins)
                .rating(ruinsCommentReq.rating())
                .comment(ruinsCommentReq.comment())
                .build();
        ruinsCommentJpaRepo.save(comment);

        List<RuinsComment> ruinsComment = ruinsCommentJpaRepo.findAllByRuins(ruins);

        Double averageRating = ruinsComment.stream()
                .mapToDouble(RuinsComment::getRating)
                .average()
                .orElse(0.0);
        averageRating = Math.round(averageRating * 10) / 10.0;

        long countComment = ruinsComment.size();

        ruins.setAverageRating(averageRating);
        ruins.setCountComment(countComment);
        ruinsJpaRepo.save(ruins);

        return RuinsCommentRes.from(comment);
    }

    public List<RuinsCommentRes> getRuinsComment(Long ruinsId) {
        Ruins ruins = ruinsJpaRepo.findById(ruinsId)
                .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

        List<RuinsComment> comments = ruinsCommentJpaRepo.findAllByRuins(ruins);

        return comments.stream()
                .map(RuinsCommentRes::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public long getMyRuinsCommentCount(Long userId) {
        return ruinsCommentJpaRepo.countByUser_UserId(userId);
    }
}
