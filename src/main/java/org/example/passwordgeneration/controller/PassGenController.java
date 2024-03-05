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
            @RequestParam("exclude_numbers") boolean exclude_numbers,
            @RequestParam("exclude_special_chars") boolean exclude_special_chars) {
        return service.createPass(length, exclude_numbers, exclude_special_chars);
    }
}
