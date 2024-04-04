package com.example.passwordgeneration.service.impl;

import com.example.passwordgeneration.dto.request.UserRequest;
import com.example.passwordgeneration.dto.response.PasswordResponse;
import com.example.passwordgeneration.dto.response.UserResponse;
import com.example.passwordgeneration.model.Password;
import com.example.passwordgeneration.model.User;
import com.example.passwordgeneration.model.Website;
import com.example.passwordgeneration.repository.PasswordRepository;
import com.example.passwordgeneration.repository.UserRepository;
import com.example.passwordgeneration.repository.WebsiteRepository;
import com.example.passwordgeneration.service.PasswordService;
import com.example.passwordgeneration.service.UserService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final PasswordRepository passwordRepository;
    private final WebsiteRepository websiteRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(),
                        user.getPassword().getRandomPassword())).toList();
    }

    @Override
    public UserResponse getUserById(Long id) {
        Optional<User> existUser = userRepository.findById(id);
        return existUser.map(user -> new UserResponse(user.getId(),user.getUsername(),
                user.getPassword().getRandomPassword())).orElse(null);
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User existUser = userRepository.findByUsername(userRequest.getUsername());
        if(existUser != null){
            return null;
        }
        PasswordResponse passwordResponse = passwordService.generatePass(userRequest.getLength(), userRequest.isExcludeNumbers(), userRequest.isExcludeSpecialChars());
        Password password = passwordRepository.findByRandomPassword(passwordResponse.getRandomPassword());
        if(password == null) {
            password = new Password(userRequest.getLength(), userRequest.isExcludeNumbers(), userRequest.isExcludeSpecialChars(), passwordResponse.getRandomPassword());
            passwordRepository.save(password);
        }
        existUser = new User(userRequest.getUsername(), password);
        userRepository.save(existUser);
        return new UserResponse(existUser.getId(), userRequest.getUsername(), password.getRandomPassword());
    }

    @Override
    public UserResponse updateUser(@PathVariable Long id, UserRequest userRequest) {
        Optional<User> existUser = userRepository.findById(id);
        if (existUser.isEmpty()) {
            return null;
        }
        PasswordResponse passwordResponse = passwordService.generatePass(userRequest.getLength(), userRequest.isExcludeNumbers(), userRequest.isExcludeSpecialChars());
        Password password = passwordRepository.findByRandomPassword(passwordResponse.getRandomPassword());
        if(password == null) {
            password = new Password(userRequest.getLength(), userRequest.isExcludeNumbers(), userRequest.isExcludeSpecialChars(), passwordResponse.getRandomPassword());
            passwordRepository.save(password);
        }
        existUser.get().setUsername(userRequest.getUsername());
        existUser.get().setPassword(password);
        existUser.get().setId(id);
        userRepository.save(existUser.get());
        return new UserResponse(existUser.get().getId(),userRequest.getUsername(), password.getRandomPassword());
    }

    @Override
    public boolean deleteUser(Long id) {
        Optional<User> existUser = userRepository.findById(id);
        if(existUser.isEmpty()){
            return false;
        }
        List<Website> websites = websiteRepository.findAll();
        for(Website website: websites){
            if(website.getUsers().contains(existUser.get())){
                website.getUsers().remove(existUser.get());
            }
        }
        userRepository.delete(existUser.get());
        return true;
    }
}

