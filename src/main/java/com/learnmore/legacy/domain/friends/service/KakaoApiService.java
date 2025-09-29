package com.learnmore.legacy.domain.friends.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoApiService {

    private final RestTemplate restTemplate;

    @Value("${kakao.api.friends-url:https://kapi.kakao.com/v1/api/talk/friends}")
    private String friendsApiUrl;

    /**
     * 카카오톡 친구 목록 조회
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getKakaoFriends(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    friendsApiUrl,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                return (List<Map<String, Object>>) responseBody.get("elements");
            }

        } catch (Exception e) {
            log.error("카카오 친구 목록 조회 실패", e);
        }

        return List.of();
    }
}