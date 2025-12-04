package com.LoginPage.login.LoginSection;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class SessionController {

    /**
     * Universal session check for frontend.
     * - Returns 200 + user info when authenticated (OAuth2User attributes OR {username})
     * - Returns 401 when not authenticated
     */
    @GetMapping("/api/session")
    public ResponseEntity<?> session(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of());
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User) {
            OAuth2User oauth = (OAuth2User) principal;
            return ResponseEntity.ok(oauth.getAttributes());
        }

        // Fallback for form-login users: return username
        String username = null;
        if (principal instanceof Principal) {
            username = ((Principal) principal).getName();
        } else {
            username = authentication.getName();
        }

        return ResponseEntity.ok(Map.of("username", username));
    }
}
