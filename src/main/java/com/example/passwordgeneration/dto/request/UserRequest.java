package com.example.passwordgeneration.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * User dto.
 */
@Setter
@Getter
@AllArgsConstructor
public class UserRequest {
  private String username;

  private int length;
  private boolean excludeNumbers;
  private boolean excludeSpecialChars;
}