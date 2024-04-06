package com.example.passwordgeneration.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "passwords")
public class Password {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int length;
    private boolean excludeNumbers;
    private boolean excludeSpecialChars;

    private String randomPassword;

    @OneToMany(mappedBy = "password", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<User> users = new HashSet<>();
    public Password(int length, boolean excludeNumbers, boolean excludeSpecialChars, String randomPassword) {
        this.length = length;
        this.excludeNumbers = excludeNumbers;
        this.excludeSpecialChars = excludeSpecialChars;
        this.randomPassword = randomPassword;
    }
}

