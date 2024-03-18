package org.example.passwordgeneration.controller;

import lombok.AllArgsConstructor;
import org.example.passwordgeneration.DTO.PasswordResponse;
import org.example.passwordgeneration.service.PasswordGenerationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/passgen")
@AllArgsConstructor
public class PasswordGenerationController {
    private final PasswordGenerationService service;
    @GetMapping("/create")
    public PasswordResponse createPass(@RequestParam("length") int length,
                                       @RequestParam("excludeNumbers") boolean excludeNumbers,
                                       @RequestParam("excludeSpecialChars") boolean excludeSpecialChars) {
        return service.createPass(length, excludeNumbers, excludeSpecialChars);
    }
}
