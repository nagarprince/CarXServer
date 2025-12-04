package com.LoginPage.login.LoginSection;

import com.LoginPage.login.SignUpSection.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final LoginSectionService service;

    public LoginController(LoginSectionService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto, HttpServletRequest req) {
        try {
            log.info("/login called with email={}", dto.getEmail());

            if (dto == null || dto.getEmail() == null || dto.getPassword() == null) {
                return ResponseEntity.badRequest().body("Missing email or password in request");
            }

            User user = service.getByEmail(dto.getEmail());
            if (user == null) {
                log.info("No user found for email={}", dto.getEmail());
                return ResponseEntity.status(400).body("Wrong EMAIL");
            }

            if (!Objects.equals(user.getPassword(), dto.getPassword())) {
                log.info("Password mismatch for email={}", dto.getEmail());
                return ResponseEntity.status(400).body("Wrong password");
            }

            // create session attribute
            HttpSession session = req.getSession(true);
            session.setAttribute("USER_EMAIL", user.getEmail());

            // ALSO set Spring Security Authentication so Authentication-based checks work
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

          session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            log.info("Login successful for email={}", user.getEmail());
            return ResponseEntity.ok("Successful Login");
        } catch (Exception ex) {
            log.error("Unhandled exception in /login", ex);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
}
