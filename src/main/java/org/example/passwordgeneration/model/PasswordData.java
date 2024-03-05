package org.example.passwordgeneration.model;

import lombok.*;

@Data
@Builder
public class PasswordData {
    private int length;
    private boolean excludeNumbers;
    private boolean excludeSpecialChars;
}
