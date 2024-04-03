package com.example.passwordgeneration.service;


import com.example.passwordgeneration.dto.request.PasswordRequest;
import com.example.passwordgeneration.dto.response.PasswordResponse;
import com.example.passwordgeneration.model.Password;

import java.util.List;

public interface PasswordService {
    List<Password> getAllPasswords();
    Password getPasswordById(Long id);
    Password createPass(int length, boolean excludeNumbers,
                        boolean excludeSpecialChars);
    Password updatePassword(Long id, PasswordRequest passwordRequest);
    boolean deletePassword(Long id);
    PasswordResponse generatePass(int length, boolean excludeNumbers,
                                  boolean excludeSpecialChars);
}