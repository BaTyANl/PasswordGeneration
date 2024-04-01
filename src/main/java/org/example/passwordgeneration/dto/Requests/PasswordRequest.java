package org.example.passwordgeneration.dto.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordRequest {
    int length;
    boolean excludeNumbers;
    boolean excludeSpecialChars;
}
