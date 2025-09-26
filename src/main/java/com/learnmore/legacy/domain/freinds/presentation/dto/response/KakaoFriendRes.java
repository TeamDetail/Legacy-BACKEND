package com.learnmore.legacy.domain.freinds.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoFriendRes(
        Long id,
        String uuid,
        @JsonProperty("profile_nickname")
        String profileNickname,
        @JsonProperty("profile_thumbnail_image")
        String profileThumbnailImage,
        Boolean favorite,
        @JsonProperty("allowed_msg")
        Boolean allowedMsg
) {
}
