package com.example.passwordgeneration.exceptions;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for exception response.
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
  private LocalDateTime localDateTime;
  private int status;
  private String message;
}
