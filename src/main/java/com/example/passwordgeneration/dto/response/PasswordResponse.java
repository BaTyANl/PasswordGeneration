package com.example.passwordgeneration.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PasswordResponse {
    private Long id;
    private String randomPassword;
}