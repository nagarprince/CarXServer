package com.LoginPage.login.MailSection;

import com.LoginPage.login.MailSection.ConfirmRequest;
import com.LoginPage.login.MailSection.MailService;
import com.LoginPage.login.MailSection.RegisterInitRequest;
import com.LoginPage.login.MailSection.VerificationService;
import com.LoginPage.login.SignUpSection.LoginService;
import com.LoginPage.login.SignUpSection.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private MailService mailService;

    @Autowired
    private LoginService loginService;

    @PostMapping("/init")
    public ResponseEntity<String> initiate(@RequestBody RegisterInitRequest req,
                                           HttpServletRequest request) {

        if (req.username == null || req.username.isEmpty() ||
                req.email == null || req.email.isEmpty() ||
                req.password == null || req.password.isEmpty()) {
            return ResponseEntity.badRequest().body("All fields required");
        }

        String emailNormalized = req.email.trim().toLowerCase();

        if (loginService.emailExists(emailNormalized)) {
            return ResponseEntity.status(409).body("Email already registered");
        }

        String code = verificationService.generateAndStoreCode(emailNormalized);

        HttpSession session = request.getSession(true);
        session.setAttribute("TEMP_USERNAME", req.username);
        session.setAttribute("TEMP_EMAIL", emailNormalized);
        session.setAttribute("TEMP_PASSWORD", req.password);

        try {
            mailService.sendVerificationEmail(emailNormalized, code);
            return ResponseEntity.ok("Verification code sent to " + emailNormalized);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send email");
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestBody ConfirmRequest req,
                                          HttpServletRequest request) {

        if (req.email == null || req.email.isEmpty() ||
                req.code == null || req.code.isEmpty()) {
            return ResponseEntity.badRequest().body("Email and code required");
        }

        String code = req.code.trim();
        if (!code.matches("\\d{4}")) {
            return ResponseEntity.badRequest().body("Code must be 4 digits");
        }

        boolean ok = verificationService.validateCode(req.email, code);
        if (!ok) {
            return ResponseEntity.status(400).body("Invalid or expired OTP");
        }


        HttpSession session = request.getSession(false);
        if (session == null)
            return ResponseEntity.status(400).body("Session expired");

        String username = (String) session.getAttribute("TEMP_USERNAME");
        String email = (String) session.getAttribute("TEMP_EMAIL");
        String password = (String) session.getAttribute("TEMP_PASSWORD");

        if (username == null || email == null || password == null) {
            return ResponseEntity.status(400).body("Registration session missing");
        }

        // ⭐ Create user & save to DB
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password); // NOTE: hash in production

        loginService.addUserData(user);

        // ⭐ Cleanup temp session data
        session.removeAttribute("TEMP_USERNAME");
        session.removeAttribute("TEMP_EMAIL");
        session.removeAttribute("TEMP_PASSWORD");

        // ⭐ Auto-login flag
        session.setAttribute("USER_EMAIL", email);

        return ResponseEntity.ok("User registered & verified successfully");
    }

    @GetMapping("/auth/check")
    public ResponseEntity<String> authCheck(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("USER_EMAIL") != null) {
            return ResponseEntity.ok("Authenticated");
        }
        return ResponseEntity.status(401).body("unauthenticated");
    }
}
