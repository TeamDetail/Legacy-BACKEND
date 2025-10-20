package com.learnmore.legacy.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "google")
public class GoogleProperties {
    private String androidClientId;
    private String iosClientId;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
