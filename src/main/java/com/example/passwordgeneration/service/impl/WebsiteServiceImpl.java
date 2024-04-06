package com.example.passwordgeneration.service.impl;


import com.example.passwordgeneration.dto.request.UserRequest;
import com.example.passwordgeneration.dto.request.WebsiteRequest;
import com.example.passwordgeneration.dto.response.PasswordResponse;
import com.example.passwordgeneration.dto.response.WebsiteResponse;
import com.example.passwordgeneration.model.Password;
import com.example.passwordgeneration.model.User;
import com.example.passwordgeneration.model.Website;
import com.example.passwordgeneration.repository.PasswordRepository;
import com.example.passwordgeneration.repository.UserRepository;
import com.example.passwordgeneration.repository.WebsiteRepository;
import com.example.passwordgeneration.service.PasswordService;
import com.example.passwordgeneration.service.WebsiteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WebsiteServiceImpl implements WebsiteService {
    private final WebsiteRepository websiteRepository;
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final PasswordRepository passwordRepository;

    @Override
    public List<WebsiteResponse> getAllWebsites() {
        List<Website> websites = websiteRepository.findAll();
        return websites.stream().map(website -> new WebsiteResponse(website.getId(),website.getWebsiteName(),
                website.getUsers().stream().map(User::getUsername).collect(Collectors.toSet()))).toList();
    }

    @Override
    public WebsiteResponse getWebsiteById(Long id) {
        Website existWebsite = websiteRepository.findById(id).orElse(null);
        if (existWebsite == null){
            return null;
        }
        return new WebsiteResponse(id, existWebsite.getWebsiteName(),
                                   existWebsite.getUsers().stream().map(User::getUsername).collect(Collectors.toSet()));
    }

    @Override
    public WebsiteResponse createWebsite(WebsiteRequest websiteRequest) {
        Set<User> users = new HashSet<>();
        Website website = new Website(websiteRequest.getWebsiteName(), users);
        for(UserRequest user:websiteRequest.getUsers()){
            User existUser = userRepository.findByUsername(user.getUsername());
            if(existUser == null){
                PasswordResponse passwordResponse = passwordService.generatePass(user.getLength(),
                        user.isExcludeNumbers(), user.isExcludeSpecialChars());
                Password password = passwordRepository.findByRandomPassword(passwordResponse.getRandomPassword());
                if(password == null) {
                    password = new Password(user.getLength(), user.isExcludeNumbers(),
                            user.isExcludeSpecialChars(), passwordResponse.getRandomPassword());
                    passwordRepository.save(password);
                }
                existUser = new User(user.getUsername(), password);
                userRepository.save(existUser);
            }
            users.add(existUser);
        }
        website.setUsers(users);
        websiteRepository.save(website);
        return new WebsiteResponse(website.getId(),website.getWebsiteName(), website.getUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toSet()));
    }

    @Override
    public WebsiteResponse addUser(Long id, UserRequest userRequest) {
        Website existWebsite = websiteRepository.findById(id).orElse(null);
        if(existWebsite == null){
            return null;
        }
        User user = userRepository.findByUsername(userRequest.getUsername());
        if(user == null){
            PasswordResponse passwordResponse = passwordService.generatePass(userRequest.getLength(),
                    userRequest.isExcludeNumbers(), userRequest.isExcludeSpecialChars());
            Password password = passwordRepository.findByRandomPassword(passwordResponse.getRandomPassword());

            if(password == null) {
                password = new Password(userRequest.getLength(), userRequest.isExcludeNumbers(),
                        userRequest.isExcludeSpecialChars(), passwordResponse.getRandomPassword());
                passwordRepository.save(password);
            }

            user = new User(userRequest.getUsername(), password);
            userRepository.save(user);
        }

        existWebsite.getUsers().add(user);
        websiteRepository.save(existWebsite);
        return new WebsiteResponse(existWebsite.getId(),existWebsite.getWebsiteName(), existWebsite.getUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toSet()));
    }

    @Override
    public WebsiteResponse removeUser(Long id, String username) {
        Website existWebsite = websiteRepository.findById(id).orElse(null);
        if(existWebsite == null){
            return null;
        }
        User existUser = existWebsite.getUsers().stream().
                filter(user -> user.getUsername().equals(username)).findFirst().orElse(null); // sql query
        if(existUser == null){
            return null;
        }
        existWebsite.getUsers().remove(existUser);
        websiteRepository.save(existWebsite);
        return new WebsiteResponse(existWebsite.getId(),existWebsite.getWebsiteName(), existWebsite.getUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toSet()));
    }

    @Override
    public boolean deleteWebsite(Long id) {
        Website existWebsite = websiteRepository.findById(id).orElse(null);
        if(existWebsite == null){
            return false;
        }
        websiteRepository.delete(existWebsite);
        return true;
    }
}
