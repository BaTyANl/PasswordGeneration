package org.example.passwordgeneration.model;

import lombok.Data;

@Data
public class PasswordGenerationData {
    private int length;
    private boolean excludeNumbers;
    private boolean excludeSpecialChars;
}
