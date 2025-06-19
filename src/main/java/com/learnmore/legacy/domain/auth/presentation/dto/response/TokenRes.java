package com.learnmore.legacy.domain.auth.presentation.dto.response;

import lombok.Builder;

@Builder
public record TokenRes(String accessToken, String refreshToken) {
}
