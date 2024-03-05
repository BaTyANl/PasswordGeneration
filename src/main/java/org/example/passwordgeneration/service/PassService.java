package org.example.passwordgeneration.service;

public interface PassService {
    String createPass(int length, boolean exclude_numbers, boolean exclude_special_chars);
}
