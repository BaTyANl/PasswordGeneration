package org.example.passwordgeneration.model;

import lombok.*;

@Data
@Builder
public class PasswordData {
    private int length;
    private boolean exclude_numbers;
    private boolean exclude_special_chars;
}
