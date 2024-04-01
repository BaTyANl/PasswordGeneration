package org.example.passwordgeneration.repository;

import org.example.passwordgeneration.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password, Long> {
}
