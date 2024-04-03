package com.example.passwordgeneration.repository;

import com.example.passwordgeneration.model.Password;
import com.example.passwordgeneration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
