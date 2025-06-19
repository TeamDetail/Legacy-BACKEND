package com.learnmore.legacy.global.security.jwt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.jwt")
public class JwtProperties {
    private Long accessExp;
    private Long refreshExp;
    private String header;
    private String prefix;
    private String secretKey;
}
