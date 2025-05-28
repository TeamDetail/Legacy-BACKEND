package com.example.jwt.domain.Kakaooauth.presentation.dto.request;

import lombok.Builder;

@Builder
public record KakaoCodeReq(String code) {}