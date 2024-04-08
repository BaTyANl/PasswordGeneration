package com.example.passwordgeneration.service;

import com.example.passwordgeneration.dto.request.PasswordRequest;
import com.example.passwordgeneration.dto.response.PasswordResponse;
import java.util.List;

/**
 * Password service interface.
 */
public interface PasswordService {

  List<PasswordResponse> getAllPasswords();

  PasswordResponse getPasswordById(Long id);

  PasswordResponse createPass(int length, boolean excludeNumbers,
                        boolean excludeSpecialChars);

  PasswordResponse updatePassword(Long id, PasswordRequest passwordRequest);

  boolean deletePassword(Long id);

  PasswordResponse generatePass(int length, boolean excludeNumbers,
                                  boolean excludeSpecialChars);
}