package com.example.passwordgeneration.dto.response;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * User response dto.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
  private Long id;
  private Set<String> websites;
  private String username;
  private String password;
}
