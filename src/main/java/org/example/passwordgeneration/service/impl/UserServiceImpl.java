package org.example.passwordgeneration.service.impl;

import lombok.AllArgsConstructor;
import org.example.passwordgeneration.dto.requests.UserRequest;
import org.example.passwordgeneration.model.User;
import org.example.passwordgeneration.repository.UserRepository;
import org.example.passwordgeneration.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User wasn't found"));
    }

    @Override
    public User createUser(UserRequest userRequest) {
        return userRepository.save(new User(userRequest.getUsername()));
    }

    @Override
    public User updateUser(@PathVariable Long id, UserRequest userRequest) {
        Optional<User> existUser = userRepository.findById(id);
        if (existUser.isEmpty()) {
            return null;
        }
        User user = new User(userRequest.getUsername());
        user.setId(id);
        return userRepository.save(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        Optional<User> existUser = userRepository.findById(id);
        if(existUser.isEmpty()){
            return false;
        }
        userRepository.delete(existUser.get());
        return true;
    }
}

