package com.learnmore.legacy.domain.card.presentation;

import com.learnmore.legacy.domain.card.presentation.dto.response.CardRes;
import com.learnmore.legacy.domain.card.service.CardService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @Operation(summary = "카드 단일 조회", description = "카드를 조회합니다.")
    @GetMapping("/{cardId}")
    public ResponseEntity<BaseResponse<CardRes>> getCard(@PathVariable Long cardId) {
        CardRes card = cardService.getCardByCardId(cardId);
        return BaseResponse.of(card);
    }

    @Operation(summary = "지역 명으로 카드 조회", description = "지역의 카드를 조회합니다.")
    @GetMapping("/collection/{region}")
    public ResponseEntity<BaseResponse<List<CardRes>>> getCardsByRegion(@PathVariable String region) {
        List<CardRes> cards = cardService.getCardsByRegion(region);
        return BaseResponse.of(cards);
    }

}