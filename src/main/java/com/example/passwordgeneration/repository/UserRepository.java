package com.example.passwordgeneration.repository;

import com.example.passwordgeneration.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * User repository.
 */
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);

  @Query(value = "SELECT * FROM users u LEFT JOIN passwords p ON u.password_id = p.id "
          + "WHERE p.random_password IS NULL OR p.length < 8 "
          + "OR p.random_password ~ '^[a-zA-Z]+$' "
          + "OR p.random_password ~ '^[0-9]+&'", nativeQuery = true)
  List<Object[]> findWithUnsafePassword();
}
