package com.example.jwt.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties(prefix = "kakao")
public class KakaoConfig {
    private String clientId;
    private String clientSecret;
    private String webRedirectUri;
    private String iosRedirectUri;
}
