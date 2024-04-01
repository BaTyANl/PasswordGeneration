package org.example.passwordgeneration.service;

import org.example.passwordgeneration.dto.Requests.PasswordRequest;
import org.example.passwordgeneration.model.Password;

import java.util.List;

public interface PasswordService {
    List<Password> getAllPasswords();
    Password getPasswordById(Long id);
    Password createPass(int length, boolean excludeNumbers,
                        boolean excludeSpecialChars);
    Password updatePassword(Long id, PasswordRequest passwordRequest);
    boolean deletePassword(Long id);
}
