package com.example.passwordgeneration.dto.response;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Website response dto.
 */
@Setter
@Getter
@AllArgsConstructor
public class WebsiteResponse {
  private Long id;
  private String websiteName;
  private Set<String> users;
}
