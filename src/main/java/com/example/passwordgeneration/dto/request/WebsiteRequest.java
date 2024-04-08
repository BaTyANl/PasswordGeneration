package com.example.passwordgeneration.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Website dto.
 */
@Setter
@Getter
@AllArgsConstructor
public class WebsiteRequest {
  String websiteName;
  private List<UserRequest> users;
}
