package com.learnmore.legacy.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.io.FileInputStream;

import javax.annotation.PostConstruct;
import java.io.IOException;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "fcm")
public class FCMConfig {
    private String serviceAccountFile;

    private String projectId;

    @PostConstruct
    public void initializeFirebase() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream(serviceAccountFile)))
                    .setProjectId(projectId)
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }
}
