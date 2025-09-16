package com.learnmore.legacy.domain.ranking.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LevelRankingRes {
    private String nickname;
    private Integer level;
    private Integer exp;
    private String imageUrl;
    private UserStyleRes title;
}
