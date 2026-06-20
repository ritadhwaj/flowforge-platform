package com.flowforge.backend.config;

import com.flowforge.backend.util.CryptoUtil;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MongoProperties.class)
public class MongoConfig {

    private final MongoProperties mongoProperties;

    public MongoConfig(MongoProperties mongoProperties) {
        this.mongoProperties = mongoProperties;
    }

    @Bean
    public MongoClient mongoClient() throws Exception {
        String encryptionKey = mongoProperties.getEncryptionKey();
        if (encryptionKey == null || encryptionKey.isBlank()) {
            throw new IllegalStateException("MONGO_ENCRYPTION_KEY is not set in .env or environment");
        }

        String username = CryptoUtil.decrypt(mongoProperties.getEncryptedUsername(), encryptionKey);
        String password = CryptoUtil.decrypt(mongoProperties.getEncryptedPassword(), encryptionKey);

        String uri = mongoProperties.getUriTemplate()
                .replace("{username}", username)
                .replace("{password}", password);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();

        return MongoClients.create(settings);
    }
}
