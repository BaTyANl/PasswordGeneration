package org.example.passwordgeneration.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordResponse {
    private String randomPassword;
}
