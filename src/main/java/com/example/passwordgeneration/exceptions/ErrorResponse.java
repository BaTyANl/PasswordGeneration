package com.example.passwordgeneration.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime localDateTime;
    private int status;
    private String message;
}
