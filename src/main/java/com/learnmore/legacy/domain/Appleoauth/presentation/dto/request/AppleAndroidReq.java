package com.learnmore.legacy.domain.Appleoauth.presentation.dto.request;

public record AppleAndroidReq(
        String code,
        String idToken
) {
}
