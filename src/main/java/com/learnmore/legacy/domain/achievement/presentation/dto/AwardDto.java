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
    private StoreType itemType;
    private String itemName;
    private String itemDescription;
    private Long itemCount;
}