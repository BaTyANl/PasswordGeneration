package com.example.passwordgeneration.controller;

import com.example.passwordgeneration.dto.request.UserRequest;
import com.example.passwordgeneration.dto.response.UserResponse;
import com.example.passwordgeneration.service.UserService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * User controller.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
  private static final String USER_NOT_FOUND = "User wasn't found";
  private final UserService userService;

  @GetMapping("/all")
  public List<UserResponse> getAllUsers() {
    return userService.getAllUsers();
  }

  /**
   * Get user by id.
   */
  @GetMapping("/id/{id}")
  public ResponseEntity<Object> getUserById(@PathVariable Long id) {
    UserResponse userResponse = userService.getUserById(id);
    if (userResponse == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
    } else {
      return ResponseEntity.ok(userResponse);
    }
  }

  /**
   * Create user with password.
   */
  @PostMapping("/create")
  public ResponseEntity<Object> createUser(@RequestBody UserRequest userRequest) {
    UserResponse userResponse = userService.createUser(userRequest);
    if (userResponse != null) {
      return ResponseEntity.ok(userResponse);
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    }
  }

  /**
   * Update user.
   */
  @PutMapping("/update/{id}")
  public ResponseEntity<Object> updateUser(@PathVariable Long id,
                                           @RequestBody UserRequest userRequest) {
    UserResponse userResponse = userService.updateUser(id, userRequest);
    if (userResponse == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
    } else {
      return ResponseEntity.ok(userResponse);
    }
  }

  /**
   * Delete user.
   */
  @DeleteMapping("/delete/{id}")
  public HttpStatus deleteUser(@PathVariable Long id) {
    boolean isExist = userService.deleteUser(id);
    if (isExist) {
      return HttpStatus.OK;
    } else {
      return HttpStatus.NOT_FOUND;
    }
  }
}

