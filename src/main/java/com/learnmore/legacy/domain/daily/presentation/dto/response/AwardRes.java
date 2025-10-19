package com.learnmore.legacy.domain.daily.presentation.dto.response;

import com.learnmore.legacy.domain.store.model.enums.StoreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardRes {
    private StoreType itemType;
    private String itemName;
    private String itemDescription;
    private String itemCount;
}
