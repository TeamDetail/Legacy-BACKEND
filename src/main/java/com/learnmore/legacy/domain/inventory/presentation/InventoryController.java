package com.learnmore.legacy.domain.inventory.presentation;

import com.learnmore.legacy.domain.card.presentation.dto.response.CardRes;
import com.learnmore.legacy.domain.inventory.presentation.dto.requset.CardpackReq;
import com.learnmore.legacy.domain.inventory.presentation.dto.requset.CreditpackReq;
import com.learnmore.legacy.domain.inventory.presentation.dto.response.CreditpackRes;
import com.learnmore.legacy.domain.inventory.presentation.dto.response.InventoryRes;
import com.learnmore.legacy.domain.inventory.service.InventoryService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import com.learnmore.legacy.global.common.repo.UserSessionHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "인벤토리", description = "인벤토리 API")
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;
    private final UserSessionHolder userSessionHolder;

    @GetMapping
    @Operation(summary = "인벤토리 조회", description = "인벤토리를 조회합니다.")
    public ResponseEntity<BaseResponse<List<InventoryRes>>> getInventory() {
        Long userId = userSessionHolder.get().getUserId();
        return BaseResponse.of(inventoryService.getInventory(userId));
    }

    @PostMapping("/cardpack")
    @Operation(summary = "카드팩 오픈", description = "특성에 맞는 카드 중 3개를 저장합니다.")
    public ResponseEntity<BaseResponse<List<CardRes>>> openCardPack(@RequestBody CardpackReq request) {
        Long userId = userSessionHolder.get().getUserId();
        return BaseResponse.of(inventoryService.openCardpack(userId, request));
    }

    @PostMapping("/credit-pack/use")
    @Operation(summary = "크레딧팩 오픈", description = "1000개의 크레딧을 획득합니다.")
    public ResponseEntity<BaseResponse<CreditpackRes>> openCreditPack(@RequestBody CreditpackReq request) {
        Long userId = userSessionHolder.get().getUserId();
        return BaseResponse.of(inventoryService.openCreditPack(userId, request));
    }

}
