package com.learnmore.legacy.domain.kakaooauth.presentation.dto.request;

import lombok.Builder;

@Builder
public record KakaoCodeReq(String code) {}