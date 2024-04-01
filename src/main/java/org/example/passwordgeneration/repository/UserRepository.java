package org.example.passwordgeneration.repository;

import org.example.passwordgeneration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
