package com.learnmore.legacy.domain.ranking.presentation.dto.response;

public record BlockRankingRes(
        String nickname,
        Integer level,
        Integer allBlocks,
        String imageUrl,
        UserStyleRes title
) {
}
