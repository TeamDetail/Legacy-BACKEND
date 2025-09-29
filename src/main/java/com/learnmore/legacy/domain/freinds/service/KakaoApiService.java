package com.learnmore.legacy.domain.freinds.service;

import com.learnmore.legacy.domain.freinds.presentation.dto.response.KakaoFriendsRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class KakaoApiService {

    private final WebClient webClient;
    private final String friendsApiUrl;

    public KakaoApiService(@Value("${kakao.api.friends-url}") String friendsApiUrl) {
        this.friendsApiUrl = friendsApiUrl;
        this.webClient = WebClient.builder()
                .baseUrl(friendsApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    /**
     * 카카오 친구 목록을 조회하는 API를 호출합니다.
     * @param accessToken 사용자 액세스 토큰
     * @return KakaoFriendsResponse
     */
    public KakaoFriendsRes getFriends(String accessToken) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder.queryParam("limit", 100).build()) // 친구 수 제한 (최대 100)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(KakaoFriendsRes.class)
                    .block(); // 비동기 응답을 동기적으로 기다립니다.
        } catch (Exception e) {
            log.error("카카오 친구 API 호출 중 오류가 발생했습니다.", e);
            // 실제 프로덕션 코드에서는 커스텀 예외를 던지는 것이 좋습니다.
            throw new RuntimeException("카카오 API 호출에 실패했습니다.", e);
        }
    }
}

