package com.learnmore.legacy.domain.block.presentation;

import com.learnmore.legacy.domain.block.presentation.dto.request.BlockAddReq;
import com.learnmore.legacy.domain.block.presentation.dto.response.BlockRes;
import com.learnmore.legacy.domain.block.service.BlockService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import com.learnmore.legacy.global.security.auth.AuthDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/block")
public class BlockController {
    private final BlockService blockService;

    @Operation(summary = "블록 추가", description = "블록을 추가합니다")
    @PostMapping
    public ResponseEntity<BaseResponse<BlockRes>> addBlock(@RequestBody BlockAddReq request, @AuthenticationPrincipal AuthDetails authDetails) {
        Long userId = authDetails.getId();
        BlockRes response = blockService.addBlock(request, userId);
        return BaseResponse.of(response);
    }

    @Operation(summary = "내 블록 조회", description = "내 블록을 조회합니다")
    @GetMapping("/user/me")
    public ResponseEntity<BaseResponse<List<BlockRes>>> getMyBlocks(@AuthenticationPrincipal AuthDetails userDetails) {
        Long userId = userDetails.getId();
        List<BlockRes> blocks = blockService.getBlocksByUserId(userId);
        return BaseResponse.of(blocks);
    }
}
