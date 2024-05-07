package com.example.passwordgeneration.service;

import com.example.passwordgeneration.dto.request.UserRequest;
import com.example.passwordgeneration.dto.response.UserResponse;
import com.example.passwordgeneration.model.User;
import java.util.List;

/**
 * Interface of user's service.
 */
public interface UserService {
  List<UserResponse> getAllUsers();

  List<UserResponse> getWithUnsafePassword();

  UserResponse getUserById(Long id);

  UserResponse createUser(UserRequest userRequest);

  List<UserResponse> createManyUsers(List<UserRequest> userRequests);

  UserResponse updateUser(Long id, UserRequest userRequest);

  void deleteUser(Long id);

  User getFromRepo(Long id);
}
