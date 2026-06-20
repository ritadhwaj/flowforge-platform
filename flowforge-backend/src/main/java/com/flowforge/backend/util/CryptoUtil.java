package com.flowforge.backend.util;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtil {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH_BYTES = 12;
    private static final int TAG_LENGTH_BITS = 128;

    // Output format: Base64(IV[12] + ciphertext + GCM-tag[16])
    public static String encrypt(String plaintext, String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        byte[] iv = new byte[IV_LENGTH_BYTES];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,
                new SecretKeySpec(keyBytes, "AES"),
                new GCMParameterSpec(TAG_LENGTH_BITS, iv));

        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        byte[] combined = new byte[IV_LENGTH_BYTES + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, IV_LENGTH_BYTES);
        System.arraycopy(encrypted, 0, combined, IV_LENGTH_BYTES, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String base64Ciphertext, String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        byte[] combined = Base64.getDecoder().decode(base64Ciphertext);

        byte[] iv = new byte[IV_LENGTH_BYTES];
        byte[] ciphertext = new byte[combined.length - IV_LENGTH_BYTES];
        System.arraycopy(combined, 0, iv, 0, IV_LENGTH_BYTES);
        System.arraycopy(combined, IV_LENGTH_BYTES, ciphertext, 0, ciphertext.length);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(keyBytes, "AES"),
                new GCMParameterSpec(TAG_LENGTH_BITS, iv));

        return new String(cipher.doFinal(ciphertext));
    }
}
