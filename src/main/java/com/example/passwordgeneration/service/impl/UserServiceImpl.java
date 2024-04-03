package com.example.passwordgeneration.service.impl;

import com.example.passwordgeneration.dto.request.UserRequest;
import com.example.passwordgeneration.dto.response.PasswordResponse;
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
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User wasn't found"));
    }

    @Override
    public User createUser(UserRequest userRequest) {
        PasswordResponse passwordResponse = passwordService.generatePass(userRequest.getLength(), userRequest.isExcludeNumbers(), userRequest.isExcludeSpecialChars());
        Password password = passwordRepository.findByRandomPassword(passwordResponse.getRandomPassword());
        if(password == null) {
            password = new Password(userRequest.getLength(), userRequest.isExcludeNumbers(), userRequest.isExcludeSpecialChars(), passwordResponse.getRandomPassword());
            passwordRepository.save(password);
        }
        User user = new User(userRequest.getUsername(), password);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(@PathVariable Long id, UserRequest userRequest) {
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
        User user = new User(userRequest.getUsername(), password);
        user.setId(id);
        return userRepository.save(user);
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

