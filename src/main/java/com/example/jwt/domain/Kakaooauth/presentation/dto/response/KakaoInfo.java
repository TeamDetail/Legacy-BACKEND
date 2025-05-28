package com.example.jwt.domain.Kakaooauth.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoInfo {
    private Long id;
    private String nickname;
    private String profileImage;

    public KakaoInfo(Long id, String nickname, String profileImage) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}