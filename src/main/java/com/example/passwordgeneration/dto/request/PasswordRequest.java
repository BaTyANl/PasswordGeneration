package com.example.passwordgeneration.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Password dto.
 */
@Getter
@Setter
@AllArgsConstructor
public class PasswordRequest {
  int length;
  boolean excludeNumbers;
  boolean excludeSpecialChars;
}
