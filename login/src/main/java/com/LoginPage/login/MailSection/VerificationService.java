package com.LoginPage.login.MailSection;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationService {

    private final ConcurrentHashMap<String, VerificationToken> store = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    private final long EXPIRY_MILLIS = TimeUnit.MINUTES.toMillis(5);

    public String generateAndStoreCode(String email) {
        String code = String.format("%04d", random.nextInt(10000));
        long expiresAt = System.currentTimeMillis() + EXPIRY_MILLIS;
        store.put(email.toLowerCase(), new VerificationToken(code, expiresAt));
        return code;
    }

    public boolean validateCode(String email, String code) {
        VerificationToken token = store.get(email.toLowerCase());
        if (token == null) return false;
        if (System.currentTimeMillis() > token.getExpiresAt()) {
            store.remove(email.toLowerCase());
            return false;
        }
        if (token.getCode().equals(code)) {
            store.remove(email.toLowerCase());
            return true;
        } else {
            return false;
        }
    }
}
