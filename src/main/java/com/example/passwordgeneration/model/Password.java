package com.example.passwordgeneration.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Password Entity.
 */
@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "passwords")
public class Password {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int length;
  private boolean excludeNumbers;
  private boolean excludeSpecialChars;

  private String randomPassword;
  @OneToMany(mappedBy = "password", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Set<User> users = new HashSet<>();
  /**
   * Password constructor.
   */

  public Password(int length, boolean excludeNumbers,
                  boolean excludeSpecialChars, String randomPassword) {
    this.length = length;
    this.excludeNumbers = excludeNumbers;
    this.excludeSpecialChars = excludeSpecialChars;
    this.randomPassword = randomPassword;
  }
}

