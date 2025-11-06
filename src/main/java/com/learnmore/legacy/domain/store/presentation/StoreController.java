package com.learnmore.legacy.domain.store.presentation;

import com.learnmore.legacy.domain.store.presentation.dto.response.CardPackRes;
import com.learnmore.legacy.domain.store.service.StoreService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "상점", description = "상점 API")
@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping("/cardpack")
    @Operation(summary = "카드팩 상점 조회", description = "카드팩 상점을 조회합니다.")
    public ResponseEntity<BaseResponse<CardPackRes>> getStorePage() {
        return BaseResponse.of(storeService.getCardPack());
    }

    @PatchMapping("/cardBuy/{cardpackId}")
    @Operation(summary = "카드팩 구매", description = "카드팩을 하나 구매합니다.")
    public ResponseEntity<BaseResponse<String>> buyCardPack(@PathVariable Long cardpackId) {
        return BaseResponse.of(storeService.buyCardPack(cardpackId) + " 크레딧을 소모하고 구매했습니다!");
    }
}
