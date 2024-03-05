package org.example.passwordgeneration.controller;


import lombok.AllArgsConstructor;
import org.example.passwordgeneration.service.PassService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/passgen")
@AllArgsConstructor
public class PassGenController {
    private final PassService service;

    @GetMapping("/create")
    public String createPass(@RequestParam("length") int length,
            @RequestParam("excludeNumbers") boolean excludeNumbers,
            @RequestParam("excludeSpecialChars") boolean excludeSpecialChars) {
        return service.createPass(length, excludeNumbers, excludeSpecialChars);
    }
}
