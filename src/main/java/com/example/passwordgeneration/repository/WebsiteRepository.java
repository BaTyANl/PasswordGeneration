package com.example.passwordgeneration.repository;

import com.example.passwordgeneration.model.User;
import com.example.passwordgeneration.model.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Website repository.
 */
public interface WebsiteRepository extends JpaRepository<Website, Long> {
  Website findByWebsiteName(String websiteName);

  @Query("SELECT u FROM Website w JOIN w.users u WHERE u.username = :username AND w.id = :id")
  User findInSetByUsername(@Param("username") String username, @Param("id") Long id);
}
