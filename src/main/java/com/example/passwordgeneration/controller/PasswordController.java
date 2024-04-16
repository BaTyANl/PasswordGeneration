package com.example.passwordgeneration.controller;

import com.example.passwordgeneration.dto.request.PasswordRequest;
import com.example.passwordgeneration.dto.response.PasswordResponse;
import com.example.passwordgeneration.service.PasswordService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Password controller.
 */
@RestController
@RequestMapping("/api/v1/password")
@AllArgsConstructor
public class PasswordController {
  private final PasswordService service;

  @GetMapping("/all")
  public List<PasswordResponse> getAllPasswords() {
    return service.getAllPasswords();
  }

  /**
   * Get Password by id method.
   */
  @GetMapping("/id/{id}")
  public ResponseEntity<PasswordResponse> getPasswordById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getPasswordById(id));
  }

  @PostMapping("/create")
  public ResponseEntity<PasswordResponse> createPass(@RequestParam("length") int length,
          @RequestParam("excludeNumbers") boolean excludeNumbers,
          @RequestParam("excludeSpecialChars") boolean excludeSpecialChars) {
    return new ResponseEntity<>(service.createPass(length,
            excludeNumbers, excludeSpecialChars), HttpStatus.OK);
  }

  /**
   * Update password method.
   */
  @PutMapping("/update/{id}")
  public ResponseEntity<PasswordResponse> updatePassword(@PathVariable Long id,
                                               @RequestBody PasswordRequest passwordRequest) {
    return ResponseEntity.ok(service.updatePassword(id, passwordRequest));
  }

  /**
   * Delete password by id.
   */
  @DeleteMapping("/delete/{id}")
  public HttpStatus deletePassword(@PathVariable Long id) {
    service.deletePassword(id);
    return HttpStatus.OK;
  }
}

