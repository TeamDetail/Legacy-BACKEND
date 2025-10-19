package com.learnmore.legacy.domain.daily.presentation.dto.request;

import com.learnmore.legacy.domain.store.model.enums.StoreType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AwardReq {
    private StoreType itemType;
    private Long itemId;
    private Integer itemCount;
}