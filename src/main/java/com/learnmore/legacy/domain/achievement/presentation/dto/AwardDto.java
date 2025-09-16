package com.learnmore.legacy.domain.achievement.presentation.dto;


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
}