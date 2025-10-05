package com.learnmore.legacy.global.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "google")
public class GoogleProperties {
    private String androidClientId;
    private String iosClientId;
    private String webClientId;
    private String webClientSecret;
    private String redirectUri;
}
