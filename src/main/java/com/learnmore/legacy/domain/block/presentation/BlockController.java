package com.learnmore.legacy.domain.block.presentation;

import com.learnmore.legacy.domain.block.presentation.dto.request.BlockAddReq;
import com.learnmore.legacy.domain.block.presentation.dto.response.BlockRes;
import com.learnmore.legacy.domain.block.service.BlockService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import com.learnmore.legacy.global.common.repo.UserSessionHolder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/block")
public class BlockController {

    private final BlockService blockService;
    private final UserSessionHolder userSessionHolder;

    @Operation(summary = "블록 추가", description = "블록을 추가합니다")
    @PostMapping
    public ResponseEntity<BaseResponse<BlockRes>> addBlock(@RequestBody BlockAddReq request) {
        Long userId = userSessionHolder.get().getUserId();
        return BaseResponse.of(blockService.addBlock(request, userId));
    }

    @Operation(summary = "내 블록 조회", description = "내 블록을 조회합니다")
    @GetMapping("/user/me")
    public ResponseEntity<BaseResponse<List<BlockRes>>> getMyBlocks() {
        Long userId = userSessionHolder.get().getUserId();
        return BaseResponse.of(blockService.getBlocksByUserId(userId));
    }
}
