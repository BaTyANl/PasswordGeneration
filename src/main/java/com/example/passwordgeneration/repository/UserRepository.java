package com.example.passwordgeneration.repository;

import com.example.passwordgeneration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User repository.
 */
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);
}