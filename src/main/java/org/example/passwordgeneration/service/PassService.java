package org.example.passwordgeneration.service;

public interface PassService {
    String createPass(int length, boolean includeUpper,
                      boolean includeLower, boolean includeSpecial,
                      boolean includeNum);
}
