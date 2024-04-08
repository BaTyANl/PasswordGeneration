package com.example.passwordgeneration.service;

import com.example.passwordgeneration.dto.request.UserRequest;
import com.example.passwordgeneration.dto.response.UserResponse;
import java.util.List;

/**
 * Interface of user's service.
 */
public interface UserService {
  List<UserResponse> getAllUsers();

  UserResponse getUserById(Long id);

  UserResponse createUser(UserRequest userRequest);

  UserResponse updateUser(Long id, UserRequest userRequest);

  boolean deleteUser(Long id);
}
