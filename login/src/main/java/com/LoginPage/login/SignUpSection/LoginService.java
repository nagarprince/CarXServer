package com.LoginPage.login.SignUpSection;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class LoginService {

    @Autowired
    private SignUpRepo repo;

    public User addUserData(User user) {
        return repo.save(user);
    }

    public boolean emailExists(String email) {
        return repo.existsByEmail(email.trim().toLowerCase());
    }
}

