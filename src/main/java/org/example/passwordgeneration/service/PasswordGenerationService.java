package org.example.passwordgeneration.service;

import org.example.passwordgeneration.dto.PasswordResponse;

public interface PasswordGenerationService {
    PasswordResponse createPass(int length, boolean excludeNumbers,
                                boolean excludeSpecialChars);
}
