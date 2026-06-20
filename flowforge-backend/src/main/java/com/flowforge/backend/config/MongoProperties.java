package com.flowforge.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.mongodb")
public class MongoProperties {
    private String uriTemplate;
    private String encryptedUsername;
    private String encryptedPassword;
    private String encryptionKey;
}
