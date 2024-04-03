package com.example.passwordgeneration.repository;

import com.example.passwordgeneration.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password, Long> {
    Password findByRandomPassword(String password);
}
