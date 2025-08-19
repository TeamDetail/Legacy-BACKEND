package com.learnmore.legacy.domain.inventory.presentation.dto.requset;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CardpackReq {
    private Long cardpackId;
    private int count;
}
