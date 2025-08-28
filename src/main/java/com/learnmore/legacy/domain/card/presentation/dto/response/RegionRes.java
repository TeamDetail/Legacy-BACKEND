package com.learnmore.legacy.domain.card.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegionRes {
    private Long maxCount;
    private List<CardRes> cards;
}
