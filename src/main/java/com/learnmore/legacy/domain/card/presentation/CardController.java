package com.learnmore.legacy.domain.card.presentation;

import com.learnmore.legacy.domain.card.presentation.dto.request.CardReq;
import com.learnmore.legacy.domain.card.presentation.dto.request.LineAttributeReq;
import com.learnmore.legacy.domain.card.presentation.dto.request.NationAttributeReq;
import com.learnmore.legacy.domain.card.presentation.dto.request.RegionAttributeReq;
import com.learnmore.legacy.domain.card.presentation.dto.response.*;
import com.learnmore.legacy.domain.card.service.CardService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @Operation(summary = "카드 모두 조회", description = "카드를 모두 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponse<List<CardRes>>> getCard(@PathVariable Long userId) {
        return BaseResponse.of(cardService.getCardByCardId(userId));
    }

    @Operation(summary = "지역 명으로 카드 조회", description = "지역의 카드를 조회합니다.")
    @GetMapping("/collection/{region}")
    public ResponseEntity<BaseResponse<CardsRes>> getCardsByRegion(@PathVariable String region) {
        return BaseResponse.of(cardService.getCardsByRegion(region));
    }

    @Operation(summary = "카드 더미 추가", description = "카드 더미를 추가합니다.")
    @PostMapping
    public ResponseEntity<BaseResponse<CardRes>> createCard(@RequestBody CardReq cardReq) {
        return BaseResponse.of(cardService.addCard(cardReq));
    }

    @Operation(summary = "국가 속성 더미 추가", description = "국가 속성 더미를 추가합니다.")
    @PostMapping("/nation")
    public ResponseEntity<BaseResponse<NationAttributeRes>> createNation(@RequestBody NationAttributeReq nationAttributeReq) {
        return BaseResponse.of(cardService.addNation(nationAttributeReq));
    }

    @Operation(summary = "개열 속성 더미 추가", description = "개열 속성 더미를 추가합니다.")
    @PostMapping("/line")
    public ResponseEntity<BaseResponse<LineAttributeRes>> createLine(@RequestBody LineAttributeReq lineAttributeReq) {
        return BaseResponse.of(cardService.addLine(lineAttributeReq));
    }

    @Operation(summary = "지역 속성 더미 추가", description = "지역 속성 더미를 추가합니다.")
    @PostMapping("/region")
    public ResponseEntity<BaseResponse<RegionAttributeRes>> createRegion(@RequestBody RegionAttributeReq regionAttributeReq) {
        return BaseResponse.of(cardService.addRegion(regionAttributeReq));
    }

}