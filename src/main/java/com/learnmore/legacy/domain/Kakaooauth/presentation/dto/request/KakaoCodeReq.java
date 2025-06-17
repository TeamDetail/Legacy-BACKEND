package com.learnmore.legacy.domain.Kakaooauth.presentation.dto.request;

import lombok.Builder;

@Builder
public record KakaoCodeReq(String code) {}