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
  private final UserService userService;

  @GetMapping("/all")
  public List<UserResponse> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/unsafe")
  public List<UserResponse> getWithUnsafePassword() {
    return userService.getWithUnsafePassword();
  }
  /**
   * Get user by id.
   */

  @GetMapping("/id/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  /**
   * Create user with password.
   */
  @PostMapping("/create")
  public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
    return ResponseEntity.ok(userService.createUser(userRequest));
  }

  @PostMapping("/create_many")
  public List<UserResponse> createManyUsers(
          @RequestBody List<UserRequest> userRequests){
    return  userService.createManyUsers(userRequests);
  }
  /**
   * Update user.
   */
  @PutMapping("/update/{id}")
  public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                           @RequestBody UserRequest userRequest) {
    return ResponseEntity.ok(userService.updateUser(id, userRequest));
  }

  /**
   * Delete user.
   */
  @DeleteMapping("/delete/{id}")
  public HttpStatus deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return HttpStatus.OK;
  }
}

