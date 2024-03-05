package org.example.passwordgeneration.service;

public interface PassService {
    String createPass(int length, boolean excludeNumbers, boolean excludeSpecialChars);
}
