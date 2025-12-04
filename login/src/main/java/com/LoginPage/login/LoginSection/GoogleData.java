package com.LoginPage.login.LoginSection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class GoogleData {

    @GetMapping("/GoogleLogin")
    public ResponseEntity<Map<String,Object>> Data(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of());
        return ResponseEntity.ok(principal.getAttributes());
    }


}
