package com.example.passwordgeneration.repository;

import com.example.passwordgeneration.model.Website;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsiteRepository extends JpaRepository<Website, Long> {
}
