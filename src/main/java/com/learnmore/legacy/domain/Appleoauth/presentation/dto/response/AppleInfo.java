package com.learnmore.legacy.domain.Appleoauth.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppleInfo {
    private String sub;
    private String email;
    private String fullName;
}

