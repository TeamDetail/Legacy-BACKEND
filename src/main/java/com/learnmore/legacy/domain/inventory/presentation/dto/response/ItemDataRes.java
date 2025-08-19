package com.learnmore.legacy.domain.inventory.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDataRes {
    private String cardpackName;
    private String cardpackContent;
    private Long cardpackId;
}
