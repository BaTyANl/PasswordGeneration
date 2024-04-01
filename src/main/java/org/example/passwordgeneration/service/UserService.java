package org.example.passwordgeneration.service;

import org.example.passwordgeneration.dto.requests.UserRequest;
import org.example.passwordgeneration.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User createUser(UserRequest userRequest);
    User updateUser(Long id, UserRequest userRequest);
    boolean deleteUser(Long id);
}
