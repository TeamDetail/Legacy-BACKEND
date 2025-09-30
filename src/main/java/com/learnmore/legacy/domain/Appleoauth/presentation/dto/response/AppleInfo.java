package com.learnmore.legacy.domain.Appleoauth.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppleInfo {
    private String sub;
    private String email;
    private String fullName;
}

