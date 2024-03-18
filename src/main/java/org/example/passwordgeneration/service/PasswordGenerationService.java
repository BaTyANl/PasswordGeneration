package org.example.passwordgeneration.service;

import org.example.passwordgeneration.DTO.PasswordResponse;

public interface PasswordGenerationService {
    PasswordResponse createPass(int length, boolean excludeNumbers,
                                boolean excludeSpecialChars);
}
