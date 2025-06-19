package com.learnmore.legacy.domain.Kakaooauth.presentation.dto.request;

import java.util.Map;

public record KakaoUserReq(
        Long id,
        KakaoAccount kakao_account,
        Map<String, Object> properties
) {
    public String nickname() {
        if (kakao_account != null && kakao_account.profile() != null) {
            return kakao_account.profile().nickname();
        }
        return "null";
    }

    public String profileImage() {
        if (kakao_account != null && kakao_account.profile() != null) {
            return kakao_account.profile().profile_image_url();
        }
        return null;
    }

    public record KakaoAccount(
            Profile profile
    ) {}

    public record Profile(
            String nickname,
            String profile_image_url
    ) {}
}

