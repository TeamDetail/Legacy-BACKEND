package com.learnmore.legacy.domain.achievement.presentation.dto;

import com.learnmore.legacy.domain.store.model.enums.StoreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public  class AwardDto {
    private Long itemId;
    private String itemType;
    private String itemName;
    private String itemDescription;
    private Long itemCount;

    public AwardDto(Long itemId, StoreType itemType, String itemName, String itemDescription, Long itemCount) {
        this.itemId = itemId;
        this.itemType = (itemType != null) ? itemType.name() : null;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemCount = itemCount;
    }
}