package com.example.passwordgeneration.service;

import com.example.passwordgeneration.dto.request.UserRequest;
import com.example.passwordgeneration.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User createUser(UserRequest userRequest);
    User updateUser(Long id, UserRequest userRequest);
    boolean deleteUser(Long id);
}
