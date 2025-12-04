package com.LoginPage.login.SignUpSection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignUpRepo extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
