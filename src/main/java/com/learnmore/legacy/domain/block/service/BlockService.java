package com.learnmore.legacy.domain.block.service;

import com.learnmore.legacy.domain.block.model.Block;
import com.learnmore.legacy.domain.block.model.BlockHistory;
import com.learnmore.legacy.domain.block.model.repo.BlockHistoryJpaRepo;
import com.learnmore.legacy.domain.block.model.repo.BlockJpaRepo;
import com.learnmore.legacy.domain.block.presentation.dto.request.BlockAddReq;
import com.learnmore.legacy.domain.block.presentation.dto.response.BlockRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockService {
    private final BlockJpaRepo blockJpaRepo;
    private final BlockHistoryJpaRepo blockHistoryJpaRepo;

    public BlockRes addBlock(BlockAddReq request) {
        Block block = Block.builder()
                .blockType(request.getBlockType())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        Block savedBlock = blockJpaRepo.save(block);

        BlockHistory blockHistory = BlockHistory.builder()
                .userId(request.getUserId())
                .block(savedBlock)
                .mobileOrWebsite(request.getMobileOrWebsite())
                .build();

        blockHistoryJpaRepo.save(blockHistory);

        return BlockRes.builder()
                .blockId(savedBlock.getBlockId())
                .blockType(savedBlock.getBlockType())
                .latitude(savedBlock.getLatitude())
                .longitude(savedBlock.getLongitude())
                .build();
    }

    @Transactional(readOnly = true)
    public List<BlockRes> getBlocksByUserId(Long userId) {
        List<Block> blocks = blockJpaRepo.findBlocksByUserId(userId);

        return blocks.stream()
                .map(block -> BlockRes.builder()
                        .blockId(block.getBlockId())
                        .blockType(block.getBlockType())
                        .latitude(block.getLatitude())
                        .longitude(block.getLongitude())
                        .build())
                .collect(Collectors.toList());
    }

}
