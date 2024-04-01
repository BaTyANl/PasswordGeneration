package org.example.passwordgeneration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PasswordResponse {
    private String randomPassword;
}
