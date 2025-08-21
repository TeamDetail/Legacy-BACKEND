package com.learnmore.legacy.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {
    private String clientId;
    private String clientSecret;
    private String webRedirectUri;
    private String iosRedirectUri;
}
