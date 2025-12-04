package com.LoginPage.login.MailSection;

public class VerificationToken {


        private final String code;
        private final long expiresAt; // epoch millis

        public VerificationToken(String code, long expiresAt) {
            this.code = code;
            this.expiresAt = expiresAt;
        }

        public String getCode() { return code; }
        public long getExpiresAt() { return expiresAt; }
    }

