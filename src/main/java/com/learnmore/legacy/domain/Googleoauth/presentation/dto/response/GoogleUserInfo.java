package com.learnmore.legacy.domain.Googleoauth.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUserInfo {
    private String sub;
    private String email;
    private String name;
    private String picture;
    private Boolean emailVerified;
}
