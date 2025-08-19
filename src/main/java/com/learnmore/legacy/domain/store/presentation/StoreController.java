package com.learnmore.legacy.domain.store.presentation;

import com.learnmore.legacy.domain.store.presentation.dto.response.CardPackRes;
import com.learnmore.legacy.domain.store.service.StoreService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import com.learnmore.legacy.global.common.repo.UserSessionHolder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final UserSessionHolder userSessionHolder;

    @GetMapping
    @Operation(summary = "카드팩 상점 조회", description = "카드팩 상점을 조회합니다.")
    public ResponseEntity<BaseResponse<CardPackRes>> getStorePage() {
        Long userId = userSessionHolder.get().getUserId();
        return BaseResponse.of(storeService.getCardPack(userId));
    }

    @PatchMapping("/cardBuy/{cardpackId}")
    @Operation(summary = "카드팩 구매", description = "카드팩을 하나 구매합니다.")
    public ResponseEntity<BaseResponse<String>> buyCardPack(@PathVariable Long cardpackId) {
        Long userId = userSessionHolder.get().getUserId();
        Integer credit = storeService.buyCardPack(userId, cardpackId);
        return BaseResponse.of(credit + " 크레딧을 소모하고 구매했습니다!");
    }
}
