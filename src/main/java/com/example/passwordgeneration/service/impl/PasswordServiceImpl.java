package com.example.passwordgeneration.service.impl;

import com.example.passwordgeneration.cache.InMemoryCache;
import com.example.passwordgeneration.dto.request.PasswordRequest;
import com.example.passwordgeneration.dto.response.PasswordResponse;
import com.example.passwordgeneration.model.Password;
import com.example.passwordgeneration.model.User;
import com.example.passwordgeneration.repository.PasswordRepository;
import com.example.passwordgeneration.repository.UserRepository;
import com.example.passwordgeneration.service.PasswordService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Password service implementation.
 */
@Service
@AllArgsConstructor
public class PasswordServiceImpl implements PasswordService {
  private final RestTemplate restTemplate;
  private final PasswordRepository repository;
  private final UserRepository userRepository;
  private final InMemoryCache cache;
  public static final String PASSWORD_KEY = "Password";
  public static final String PASSWORD_NOT_EXIST = "This password does not exist: ";

  @Override
  public List<PasswordResponse> getAllPasswords() {
    return repository.findAll().stream()
    .map(password -> new PasswordResponse(password.getId(),
            password.getRandomPassword())).toList();
  }

  @Override
  public PasswordResponse getPasswordById(Long id) {
    Password existPassword = (Password) cache.get(PASSWORD_KEY + id);
    if (existPassword == null) {
      existPassword = repository.findById(id).orElseThrow(
              () -> new NoSuchElementException(PASSWORD_NOT_EXIST + id));
    }
    cache.put(PASSWORD_KEY + existPassword.getId(), existPassword);
    return new PasswordResponse(id, existPassword.getRandomPassword());
  }

  @SneakyThrows
  @Override
  public String generatePass(int length, boolean excludeNumbers,
                                       boolean excludeSpecialChars) {
    Properties properties = new Properties();
    String apiKey;
    try (FileInputStream fileInputStream = new FileInputStream("apikey.properties")) {
      properties.load(fileInputStream);
      apiKey = properties.getProperty("apiKey");
    } catch (Exception e) {
      throw new ConcurrentModificationException("File opening error");
    }

    String url = "https://api.api-ninjas.com/v1/passwordgenerator?length=" + length
            + "&exclude_numbers=" + excludeNumbers + "&exclude_special_chars=" + excludeSpecialChars
            + "&X-Api-Key=" + apiKey;
    String jsonStr = restTemplate.getForObject(url, String.class);
    if (jsonStr == null) {
      return "qwerty123";
    }
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode root = objectMapper.readTree(jsonStr);
    return root.path("random_password").asText();
  }

  @Override
  public PasswordResponse createPass(int length,
                                     boolean excludeNumbers, boolean excludeSpecialChars) {
    Password password = new Password(length, excludeNumbers, excludeSpecialChars,
              generatePass(length, excludeNumbers, excludeSpecialChars));
    Password existPassword = repository.findByRandomPassword(password.getRandomPassword());
    if (existPassword == null) {
      repository.save(password);
      cache.put(PASSWORD_KEY + password.getId(), password);
      return new PasswordResponse(password.getId(), password.getRandomPassword());
    }
    return new PasswordResponse(existPassword.getId(), existPassword.getRandomPassword());
  }

  @Override
  public PasswordResponse updatePassword(Long id, PasswordRequest passwordRequest) {
    Password existPassword = (Password) cache.get(PASSWORD_KEY + id);
    if (existPassword == null) {
      existPassword = repository.findById(id).orElseThrow(
              () -> new NoSuchElementException(PASSWORD_NOT_EXIST + id));
    }
    existPassword.setLength(passwordRequest.getLength());
    existPassword.setExcludeNumbers(passwordRequest.isExcludeNumbers());
    existPassword.setExcludeSpecialChars(passwordRequest.isExcludeSpecialChars());
    existPassword.setRandomPassword(generatePass(
        passwordRequest.getLength(), passwordRequest.isExcludeNumbers(),
        passwordRequest.isExcludeSpecialChars()));
    repository.save(existPassword);
    cache.put(PASSWORD_KEY + existPassword.getId(), existPassword);
    return new PasswordResponse(id, existPassword.getRandomPassword());
  }

  @Override
  public void deletePassword(Long id) {
    Password existPassword = (Password) cache.get(PASSWORD_KEY + id);
    if (existPassword == null) {
      existPassword = repository.findById(id).orElseThrow(
              () -> new NoSuchElementException(PASSWORD_NOT_EXIST + id));
    }
    List<User> users = userRepository.findAll();
    for (User user : users) {
      if (user.getPassword() != null && user.getPassword().equals(existPassword)) {
        user.setPassword(null);
        cache.remove(UserServiceImpl.USER_KEY + user.getId());
      }
    }
    cache.remove(PASSWORD_KEY + existPassword.getId());
    repository.delete(existPassword);
  }
}
