package com.learnmore.legacy.domain.block.presentation;

import com.learnmore.legacy.domain.block.presentation.dto.request.BlockAddReq;
import com.learnmore.legacy.domain.block.presentation.dto.response.BlockRes;
import com.learnmore.legacy.domain.block.service.BlockService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/block")
public class BlockController {
    private final BlockService blockService;

    @PostMapping
    public ResponseEntity<BaseResponse<BlockRes>> createBlock(@RequestBody BlockAddReq request) {
        BlockRes response = blockService.addBlock(request);
        return BaseResponse.of(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<List<BlockRes>>> getBlocksByUserId(@PathVariable Long userId) {
        List<BlockRes> blocks = blockService.getBlocksByUserId(userId);
        return BaseResponse.of(blocks);
    }
}
