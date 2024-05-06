package com.example.passwordgeneration.service.impl;

import com.example.passwordgeneration.cache.InMemoryCache;
import com.example.passwordgeneration.dto.request.UserRequest;
import com.example.passwordgeneration.dto.response.UserResponse;
import com.example.passwordgeneration.model.Password;
import com.example.passwordgeneration.model.User;
import com.example.passwordgeneration.model.Website;
import com.example.passwordgeneration.repository.PasswordRepository;
import com.example.passwordgeneration.repository.UserRepository;
import com.example.passwordgeneration.repository.WebsiteRepository;
import com.example.passwordgeneration.service.PasswordService;
import com.example.passwordgeneration.service.UserService;
import jakarta.transaction.Transactional;
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
  public static final String NO_PASSWORD = "no password";
  public static final String USER_NOT_EXIST = "This user does not exist: ";

  @Override
  public List<UserResponse> getAllUsers() {
    List<User> users = userRepository.findAll();
    return users.stream().map(user -> new UserResponse(user.getId(),
            user.getWebsite().stream().map(Website::getWebsiteName).collect(Collectors.toSet()),
            user.getUsername(),
            user.getPassword() != null
                    ? user.getPassword().getRandomPassword() : NO_PASSWORD)).toList();
  }

  public User getFromRepo(Long id) {
    User existUser = (User) cache.get(USER_KEY + id);
    if (existUser == null) {
      existUser = userRepository.findById(id).orElseThrow(
              () -> new NoSuchElementException(USER_NOT_EXIST + id));
    }
    return existUser;
  }

  /**
   * Get all users with unsafe password.
   */
  public List<UserResponse> getWithUnsafePassword() {
    List<Object[]> users = userRepository.findWithUnsafePassword();
    return users.stream().map(user -> new UserResponse((Long) user[0],
            getFromRepo((Long) user[0]).getWebsite().stream()
                    .map(Website::getWebsiteName).collect(Collectors.toSet()),
            (String) user[2], user[7] != null
            ? getFromRepo((Long) user[0]).getPassword()
                    .getRandomPassword() : NO_PASSWORD)).toList();
  }

  @Override
  public UserResponse getUserById(Long id) {
    User existUser = (User) cache.get(USER_KEY + id);
    if (existUser == null) {
      existUser = userRepository.findById(id).orElseThrow(
              () -> new NoSuchElementException(USER_NOT_EXIST + id));
    }
    cache.put(USER_KEY + existUser.getId(), existUser);
    return new UserResponse(existUser.getId(),
         existUser.getWebsite().stream().map(Website::getWebsiteName).collect(Collectors.toSet()),
         existUser.getUsername(),
         existUser.getPassword() != null
                 ? existUser.getPassword().getRandomPassword() : NO_PASSWORD);
  }

  @Override
  public UserResponse createUser(UserRequest userRequest) {
    User existUser = userRepository.findByUsername(userRequest.getUsername());
    if (existUser != null) {
      throw new ConcurrentModificationException(
              "This user already exists: " + userRequest.getUsername());
    }
    String passwordResponse = passwordService
            .generatePass(userRequest.getLength(),
                          userRequest.isExcludeNumbers(),
                          userRequest.isExcludeSpecialChars());
    Password password = passwordRepository
            .findByRandomPassword(passwordResponse);
    if (password == null) {
      password = new Password(userRequest.getLength(),
                              userRequest.isExcludeNumbers(),
                              userRequest.isExcludeSpecialChars(),
                              passwordResponse);
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
  @Transactional
  public List<UserResponse> createManyUsers(List<UserRequest> userRequests) {
    return userRequests.stream()
            .map(this::createUser).toList();
  }


  @Override
  public UserResponse updateUser(@PathVariable Long id, UserRequest userRequest) {
    User existUser = (User) cache.get(USER_KEY + id);
    if (existUser == null) {
      existUser = userRepository.findById(id).orElseThrow(
              () -> new NoSuchElementException(USER_NOT_EXIST + id));
    }
    if (userRepository.findByUsername(userRequest.getUsername()) != null) {
      throw new ConcurrentModificationException(
              "This user already exists: " + userRequest.getUsername());
    }
    String passwordResponse = passwordService
            .generatePass(userRequest.getLength(),
                          userRequest.isExcludeNumbers(),
                          userRequest.isExcludeSpecialChars());

    Password password = existUser.getPassword();
    if (password == null) {
      password = new Password(userRequest.getLength(),
                              userRequest.isExcludeNumbers(),
                              userRequest.isExcludeSpecialChars(),
                              passwordResponse);
      passwordRepository.save(password);
      cache.put(PasswordServiceImpl.PASSWORD_KEY + password.getId(), password);
    } else {
      password.setRandomPassword(passwordResponse);
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
  public void deleteUser(Long id) {
    User existUser = (User) cache.get(USER_KEY + id);
    if (existUser == null) {
      existUser = userRepository.findById(id).orElseThrow(
              () -> new NoSuchElementException(USER_NOT_EXIST + id));
    }
    List<Website> websites = websiteRepository.findAll();
    for (Website website : websites) {
      if (website.getUsers().contains(existUser)) {
        website.getUsers().remove(existUser);
      }
    }
    cache.remove(USER_KEY + existUser.getId());
    userRepository.delete(existUser);
  }
}

