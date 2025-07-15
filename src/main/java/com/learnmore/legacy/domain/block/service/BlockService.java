package com.learnmore.legacy.domain.block.service;

import com.learnmore.legacy.domain.block.error.BlockError;
import com.learnmore.legacy.domain.block.model.Block;
import com.learnmore.legacy.domain.block.model.BlockHistory;
import com.learnmore.legacy.domain.block.model.enums.BlockType;
import com.learnmore.legacy.domain.block.model.repo.BlockHistoryJpaRepo;
import com.learnmore.legacy.domain.block.model.repo.BlockJpaRepo;
import com.learnmore.legacy.domain.block.presentation.dto.request.BlockAddReq;
import com.learnmore.legacy.domain.block.presentation.dto.response.BlockRes;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockService {
    private final BlockJpaRepo blockJpaRepo;
    private final BlockHistoryJpaRepo blockHistoryJpaRepo;

    public BlockRes addBlock(BlockAddReq request, Long userId) {
        List<Block> existingBlocks = blockJpaRepo.findAllByLatitudeAndLongitude(
                request.getLatitude(), request.getLongitude()
        );
        if (!existingBlocks.isEmpty()) {
            throw new CustomException(BlockError.BLOCK_ALREADY_EXISTS);
        }

        Block block = Block.builder()
                .blockType(BlockType.NORMAL)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        Block savedBlock = blockJpaRepo.save(block);

        BlockHistory blockHistory = BlockHistory.builder()
                .userId(userId)
                .block(savedBlock)
                .build();

        blockHistoryJpaRepo.save(blockHistory);

        return BlockRes.from(savedBlock);
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

    @Transactional
    public void createBlockWithHistory(Long ruinsId, Long userId, BigDecimal latitude, BigDecimal longitude) {
        boolean alreadyReceived = blockHistoryJpaRepo.existsByUserIdAndBlock_BlockId(userId, ruinsId);
        if (alreadyReceived) return;

        Block block = Block.builder()
                .blockType(BlockType.RUINS)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        BlockHistory history = BlockHistory.builder()
                .userId(userId)
                .block(block)
                .build();

        blockJpaRepo.save(block);
        blockHistoryJpaRepo.save(history);
    }

}
