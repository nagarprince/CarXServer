package com.LoginPage.login.SignUpSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@CrossOrigin(origins = "http://localhost:5173")
public class RegisterComponent {

    private final LoginService service;

    @Autowired
    public RegisterComponent(LoginService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody User user) {

        service.addUserData(user);
        System.out.println(user);
        return ResponseEntity.ok("User registered successfully");
    }



}
