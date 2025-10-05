package com.learnmore.legacy.domain.kakaooauth.presentation.dto.response;

public record KakaoTokenRes(
        String access_token,
        String token_type,
        String refresh_token,
        Long expires_in,
        String scope
) {
}
