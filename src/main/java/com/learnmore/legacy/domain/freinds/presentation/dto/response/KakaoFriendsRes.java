package com.learnmore.legacy.domain.freinds.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KakaoFriendsRes(

        /**
         * 친구 목록
         */
        @JsonProperty("elements")
        List<KakaoFriendRes> elements,

        /**
         * 전체 친구 수
         */
        @JsonProperty("total_count")
        Integer totalCount,

        /**
         * 즐겨찾기 친구 수
         */
        @JsonProperty("favorite_count")
        Integer favoriteCount,

        /**
         * 이전 페이지 URL (페이징 시 사용)
         */
        @JsonProperty("before_url")
        String beforeUrl,

        /**
         * 다음 페이지 URL (페이징 시 사용)
         */
        @JsonProperty("after_url")
        String afterUrl,

        /**
         * API 호출 결과 코드 (성공 시 'ok')
         */
        @JsonProperty("result_code")
        String resultCode
) {
}
