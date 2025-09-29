package com.learnmore.legacy.domain.Appleoauth.presentation.dto.request;

import lombok.Builder;

@Builder
public record AppleCodeReq (
        String code
) {}
