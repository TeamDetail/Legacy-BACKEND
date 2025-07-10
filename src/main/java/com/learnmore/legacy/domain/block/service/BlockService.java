package com.learnmore.legacy.domain.block.service;

import com.learnmore.legacy.domain.block.model.Block;
import com.learnmore.legacy.domain.block.model.BlockHistory;
import com.learnmore.legacy.domain.block.model.enums.BlockType;
import com.learnmore.legacy.domain.block.model.repo.BlockHistoryJpaRepo;
import com.learnmore.legacy.domain.block.model.repo.BlockJpaRepo;
import com.learnmore.legacy.domain.block.presentation.dto.request.BlockAddReq;
import com.learnmore.legacy.domain.block.presentation.dto.response.BlockRes;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import com.learnmore.legacy.domain.ruins.model.repo.RuinsJpaRepo;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import jakarta.persistence.EntityNotFoundException;
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
    private final RuinsJpaRepo ruinsJpaRepo;
    private final UserJpaRepo userJpaRepo;

    public BlockRes addBlock(BlockAddReq request, Long userId) {
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
                        .latitude(block.getLatitude())
                        .longitude(block.getLongitude())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void createBlockWithHistory(Long ruinsId, Long userId) {
        boolean alreadyReceived = blockHistoryJpaRepo.existsByUserIdAndBlock_RuinsId(userId, ruinsId);
        if (alreadyReceived) return;

        Ruins ruins = ruinsJpaRepo.findById(ruinsId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유적지를 찾을 수 없습니다."));

        Block block = Block.builder()
                .blockType(BlockType.RUINS)
                .latitude(ruins.getLatitude())
                .longitude(ruins.getLongitude())
                .build();

        BlockHistory history = BlockHistory.builder()
                .userId(userId)
                .block(block)
                .build();

        block.getBlockHistories().add(history);

        blockJpaRepo.save(block);
    }

}
