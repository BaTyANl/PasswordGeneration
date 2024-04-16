package com.example.passwordgeneration.service.impl;

import com.example.passwordgeneration.cache.InMemoryCache;
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

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Implementation of user's service.
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordService passwordService;
  private final PasswordRepository passwordRepository;
  private final WebsiteRepository websiteRepository;
  private final InMemoryCache cache;
  public static final String USER_KEY = "User";
  public static final String USER_NOT_EXIST = "This user does not exist: ";

  @Override
  public List<UserResponse> getAllUsers() {
    List<User> users = userRepository.findAll();
    return users.stream().map(user -> new UserResponse(user.getId(),
            user.getWebsite().stream().map(Website::getWebsiteName).collect(Collectors.toSet()),
            user.getUsername(),
            user.getPassword() != null
                    ? user.getPassword().getRandomPassword() : "no password")).toList();
  }

  @Override
  public UserResponse getUserById(Long id) {
    User existUser = (User) cache.get(USER_KEY + id);
    if (existUser == null) {
      existUser = userRepository.findById(id).orElseThrow(
              ()->new NoSuchElementException(USER_NOT_EXIST + id));
    }
    cache.put(USER_KEY + existUser.getId(), existUser);
    return new UserResponse(existUser.getId(),
         existUser.getWebsite().stream().map(Website::getWebsiteName).collect(Collectors.toSet()),
         existUser.getUsername(),
         existUser.getPassword() != null
                 ? existUser.getPassword().getRandomPassword() : "no password");
  }

  @Override
  public UserResponse createUser(UserRequest userRequest) {
    User existUser = userRepository.findByUsername(userRequest.getUsername());
    if (existUser != null) {
      throw new ConcurrentModificationException(USER_NOT_EXIST + userRequest.getUsername());
    }
    PasswordResponse passwordResponse = passwordService
            .generatePass(userRequest.getLength(),
                          userRequest.isExcludeNumbers(),
                          userRequest.isExcludeSpecialChars());
    Password password = passwordRepository
            .findByRandomPassword(passwordResponse.getRandomPassword());
    if (password == null) {
      password = new Password(userRequest.getLength(),
                              userRequest.isExcludeNumbers(),
                              userRequest.isExcludeSpecialChars(),
                              passwordResponse.getRandomPassword());
      passwordRepository.save(password);
      cache.put(PasswordServiceImpl.PASSWORD_KEY + password.getId(), password);
    }
    existUser = new User(userRequest.getUsername(), password);
    cache.put(USER_KEY + existUser.getId(), existUser);
    userRepository.save(existUser);
    return new UserResponse(existUser.getId(),
                            existUser.getWebsite().stream()
                                    .map(Website::getWebsiteName).collect(Collectors.toSet()),
                            userRequest.getUsername(),
                            password.getRandomPassword());
  }

  @Override
  public UserResponse updateUser(@PathVariable Long id, UserRequest userRequest) {
    User existUser = (User) cache.get(USER_KEY + id);
    if (existUser == null) {
      existUser = userRepository.findById(id).orElseThrow(
              ()-> new NoSuchElementException(USER_NOT_EXIST + id));
    }
    if (userRepository.findByUsername(userRequest.getUsername()) != null){
      throw new ConcurrentModificationException("This user already exists: " + userRequest.getUsername());
    }
    PasswordResponse passwordResponse = passwordService
            .generatePass(userRequest.getLength(),
                          userRequest.isExcludeNumbers(),
                          userRequest.isExcludeSpecialChars());

    Password password = existUser.getPassword();
    if (password == null) {
      password = new Password(userRequest.getLength(),
                              userRequest.isExcludeNumbers(),
                              userRequest.isExcludeSpecialChars(),
                              passwordResponse.getRandomPassword());
      passwordRepository.save(password);
      cache.put(PasswordServiceImpl.PASSWORD_KEY + password.getId(), password);
    } else {
      password.setRandomPassword(passwordResponse.getRandomPassword());
    }
    existUser.setUsername(userRequest.getUsername());
    existUser.setPassword(password);
    existUser.setId(id);
    userRepository.save(existUser);
    cache.put(USER_KEY + id, existUser);
    return new UserResponse(existUser.getId(),
                            existUser.getWebsite().stream()
                                    .map(Website::getWebsiteName).collect(Collectors.toSet()),
                            userRequest.getUsername(),
                            password.getRandomPassword());
  }

  @Override
  public boolean deleteUser(Long id) {
    User existUser = (User) cache.get(USER_KEY + id);
    if (existUser == null) {
      existUser = userRepository.findById(id).orElseThrow(
              ()-> new NoSuchElementException(USER_NOT_EXIST + id));
    }
    List<Website> websites = websiteRepository.findAll();
    for (Website website : websites) {
      if (website.getUsers().contains(existUser)) {
        website.getUsers().remove(existUser);
      }
    }
    cache.remove(USER_KEY + existUser.getId());
    userRepository.delete(existUser);
    return true;
  }
}

