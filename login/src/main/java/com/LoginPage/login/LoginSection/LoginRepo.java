package com.LoginPage.login.LoginSection;

import com.LoginPage.login.SignUpSection.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginRepo extends JpaRepository<User, Integer> {
    Optional<User> findFirstByEmail(String email);
}
