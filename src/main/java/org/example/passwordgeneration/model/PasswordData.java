package org.example.passwordgeneration.model;

import lombok.Data;

@Data
public class PasswordData {
    private int length;
    private boolean includeUpper;
    private boolean includeLower;
    private boolean includeSpecial;
    private boolean includeNum;
}
