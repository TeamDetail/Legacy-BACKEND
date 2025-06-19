package com.learnmore.legacy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class legacyApplication {

    public static void main(String[] args) {
        SpringApplication.run(legacyApplication.class, args);
    }

}
