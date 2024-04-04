package com.example.passwordgeneration.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class WebsiteResponse {
    private Long id;
    private String websiteName;
    private Set<String> users;
}
