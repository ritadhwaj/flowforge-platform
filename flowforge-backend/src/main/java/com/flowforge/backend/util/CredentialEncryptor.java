package com.flowforge.backend.util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * One-time utility — run main() to generate encrypted credentials for application.yaml.
 * Never commit plaintext credentials; only copy the ENC(...) output values.
 */
public class CredentialEncryptor {

    public static void main(String[] args) throws Exception {
        // Change these two values before running
        String username = "YOUR_MONGO_USERNAME";
        String password = "YOUR_MONGO_PASSWORD";

        // Generate a fresh 256-bit AES key
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);

        String encryptedUsername = CryptoUtil.encrypt(username, base64Key);
        String encryptedPassword = CryptoUtil.encrypt(password, base64Key);

        System.out.println("=== Copy these into application.yaml ===");
        System.out.println("app.mongodb.encrypted-username: " + encryptedUsername);
        System.out.println("app.mongodb.encrypted-password: " + encryptedPassword);
        System.out.println();
        System.out.println("=== Set this as environment variable ===");
        System.out.println("MONGO_ENCRYPTION_KEY=" + base64Key);
    }
}
