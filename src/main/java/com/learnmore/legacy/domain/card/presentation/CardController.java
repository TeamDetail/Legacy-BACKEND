package com.learnmore.legacy.domain.card.presentation;

import com.learnmore.legacy.domain.card.presentation.dto.response.CardRes;
import com.learnmore.legacy.domain.card.presentation.dto.response.RegionRes;
import com.learnmore.legacy.domain.card.service.CardService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import com.learnmore.legacy.global.common.repo.UserSessionHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "카드", description = "카드 API")
@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;
    private final UserSessionHolder userSessionHolder;

    @Operation(summary = "카드 모두 조회", description = "카드를 모두 조회합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<List<CardRes>>> getCard() {
        Long userId = userSessionHolder.get().getUserId();
        return BaseResponse.of(cardService.getCardByCardId(userId));
    }

    @Operation(summary = "지역 명으로 카드 조회", description = "지역의 카드를 조회합니다.")
    @GetMapping("/collection/{region}")
    public ResponseEntity<BaseResponse<RegionRes>> getCardsByRegion(@PathVariable String region) {
        Long userId = userSessionHolder.get().getUserId();
        return BaseResponse.of(cardService.getCardsByRegion(region, userId));
    }
}