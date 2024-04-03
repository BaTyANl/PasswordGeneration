package com.example.passwordgeneration.repository;

import com.example.passwordgeneration.model.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface WebsiteRepository extends JpaRepository<Website, Long> {
    Website findByWebsiteName(String websiteName);
}
