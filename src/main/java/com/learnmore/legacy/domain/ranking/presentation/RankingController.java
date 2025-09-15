package com.learnmore.legacy.domain.ranking.presentation;

import com.learnmore.legacy.domain.ranking.model.enums.RankingType;
import com.learnmore.legacy.domain.ranking.presentation.dto.response.BlockRankingRes;
import com.learnmore.legacy.domain.ranking.presentation.dto.response.LevelRankingRes;
import com.learnmore.legacy.domain.ranking.usecase.RankingUseCase;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ranking")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ranklist")
public class RankingController {
    private final RankingUseCase rankingUseCase;

    @Operation(summary = "탐험 랭킹 조회", description = "탐험 랭킹을 조회합니다.")
    @GetMapping("/explore/all")
    public ResponseEntity<BaseResponse<List<BlockRankingRes>>> getExploreRanking() {
        return BaseResponse.of(rankingUseCase.getTopUserRanking());
    }

    @Operation(summary = "숙련 랭킹 조회", description = "숙련 랭킹을 조회합니다.")
    @GetMapping("/level/{type}")
    public ResponseEntity<BaseResponse<List<LevelRankingRes>>> getLevelRanking(@PathVariable RankingType type) {
        return BaseResponse.of(rankingUseCase.getTopUserLevelRanking(type));
    }
}
