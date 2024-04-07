package com.example.passwordgeneration.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private Set<String> websites;
    private String username;
    private String password;
}
