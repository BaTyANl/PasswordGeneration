package com.example.passwordgeneration.service.impl;


import com.example.passwordgeneration.cache.InMemoryCache;
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

import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of website's service.
 */
@Service
@AllArgsConstructor
public class WebsiteServiceImpl implements WebsiteService {
  private final WebsiteRepository websiteRepository;
  private final UserRepository userRepository;
  private final PasswordService passwordService;
  private final PasswordRepository passwordRepository;
  private InMemoryCache cache;
  public static final String WEBSITE_KEY = "Website";
  public static final String WEBSITE_NOT_EXIST = "This website does not exist: ";

  @Override
  public List<WebsiteResponse> getAllWebsites() {
    List<Website> websites = websiteRepository.findAll();
    return websites.stream()
            .map(website -> new WebsiteResponse(website.getId(), website.getWebsiteName(),
        website.getUsers().stream().map(User::getUsername).collect(Collectors.toSet()))).toList();
  }

  @Override
  public WebsiteResponse getWebsiteById(Long id) {
    Website existWebsite = (Website) cache.get(WEBSITE_KEY + id);
    if (existWebsite == null) {
      existWebsite = websiteRepository.findById(id).orElseThrow(
              ()-> new NoSuchElementException(WEBSITE_NOT_EXIST + id));
    }
    cache.put(WEBSITE_KEY + id, existWebsite);
    return new WebsiteResponse(id, existWebsite.getWebsiteName(),
                               existWebsite.getUsers().stream()
                                       .map(User::getUsername).collect(Collectors.toSet()));
  }

  @Override
  public WebsiteResponse createWebsite(WebsiteRequest websiteRequest) {
    if (websiteRepository.findByWebsiteName(websiteRequest.getWebsiteName()) != null){
      throw new ConcurrentModificationException("This website already exists: " + websiteRequest.getWebsiteName());
    }
    Set<User> users = new HashSet<>();
    Website website = new Website(websiteRequest.getWebsiteName(), users);

    for (UserRequest user : websiteRequest.getUsers()) {
      User existUser = userRepository.findByUsername(user.getUsername());

      if (existUser == null) {
        PasswordResponse passwordResponse = passwordService
                .generatePass(user.getLength(),
                              user.isExcludeNumbers(), user.isExcludeSpecialChars());
        Password password = passwordRepository
                .findByRandomPassword(passwordResponse.getRandomPassword());

        if (password == null) {
          password = new Password(user.getLength(), user.isExcludeNumbers(),
                                  user.isExcludeSpecialChars(),
                                  passwordResponse.getRandomPassword());
          passwordRepository.save(password);
          cache.put(PasswordServiceImpl.PASSWORD_KEY + password.getId(), password);
        }

        existUser = new User(user.getUsername(), password);
        userRepository.save(existUser);
        cache.put(UserServiceImpl.USER_KEY + existUser.getId(), existUser);
      }

      users.add(existUser);
    }
    website.setUsers(users);
    websiteRepository.save(website);
    cache.put(WEBSITE_KEY + website.getId(), website);
    return new WebsiteResponse(website.getId(), website.getWebsiteName(),
            website.getUsers().stream().map(User::getUsername).collect(Collectors.toSet()));
  }

  @Override
  public WebsiteResponse addUser(Long id, UserRequest userRequest) {
    Website existWebsite = (Website) cache.get(WEBSITE_KEY + id);
    if (existWebsite == null) {
      existWebsite = websiteRepository.findById(id).orElseThrow(
              ()-> new NoSuchElementException(WEBSITE_NOT_EXIST + id));
    }

    User user = userRepository.findByUsername(userRequest.getUsername());
    if (websiteRepository.findInSetByUsername(userRequest.getUsername()) != null){
      throw new ConcurrentModificationException("User already exists in this website: " + userRequest.getUsername());
    }
    if (user == null) {
      PasswordResponse passwordResponse = passwordService
              .generatePass(userRequest.getLength(),
                            userRequest.isExcludeNumbers(),
                            userRequest.isExcludeSpecialChars());
      Password password = passwordRepository
              .findByRandomPassword(passwordResponse.getRandomPassword());

      if (password == null) {
        password = new Password(userRequest.getLength(), userRequest.isExcludeNumbers(),
                                userRequest.isExcludeSpecialChars(),
                                passwordResponse.getRandomPassword());
        passwordRepository.save(password);
        cache.put(PasswordServiceImpl.PASSWORD_KEY + password.getId(), password);
      }
      user = new User(userRequest.getUsername(), password);
      userRepository.save(user);
      cache.put(UserServiceImpl.USER_KEY + user.getId(), user);
    }
    existWebsite.getUsers().add(user);
    websiteRepository.save(existWebsite);
    cache.put(WEBSITE_KEY + existWebsite.getId(), existWebsite);

    return new WebsiteResponse(existWebsite.getId(), existWebsite.getWebsiteName(),
            existWebsite.getUsers().stream().map(User::getUsername).collect(Collectors.toSet()));
  }

  @Override
  public WebsiteResponse removeUser(Long id, String username) {
    Website existWebsite = (Website) cache.get(WEBSITE_KEY + id);

    if (existWebsite == null) {
      existWebsite = websiteRepository.findById(id).orElseThrow(
              ()-> new NoSuchElementException(WEBSITE_NOT_EXIST + id));
    }

    User existUser = websiteRepository.findInSetByUsername(username);
    if (existUser == null) {
      throw new NoSuchElementException("User does not exist: " + username);
    }

    existWebsite.getUsers().remove(existUser);
    websiteRepository.save(existWebsite);
    cache.put(WEBSITE_KEY + id, existWebsite);
    return new WebsiteResponse(existWebsite.getId(), existWebsite.getWebsiteName(),
            existWebsite.getUsers().stream().map(User::getUsername).collect(Collectors.toSet()));
  }

  @Override
  public boolean deleteWebsite(Long id) {
    Website existWebsite = (Website) cache.get(WEBSITE_KEY + id);

    if (existWebsite == null) {
      existWebsite = websiteRepository.findById(id).orElseThrow(
              ()-> new NoSuchElementException(WEBSITE_NOT_EXIST + id));
    }

    cache.remove(WEBSITE_KEY + existWebsite);
    websiteRepository.delete(existWebsite);
    return true;
  }
}
