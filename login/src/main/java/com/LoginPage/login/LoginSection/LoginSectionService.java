package com.LoginPage.login.LoginSection;

import com.LoginPage.login.SignUpSection.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginSectionService {

    @Autowired
    private LoginRepo repo;

    public User getByEmail(String email) {
        return repo.findFirstByEmail(email).orElse(null);
    }

}
