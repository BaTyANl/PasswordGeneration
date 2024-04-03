package com.example.passwordgeneration.service.impl;

import com.example.passwordgeneration.dto.request.PasswordRequest;
import com.example.passwordgeneration.dto.response.PasswordResponse;
import com.example.passwordgeneration.model.Password;
import com.example.passwordgeneration.model.User;
import com.example.passwordgeneration.repository.PasswordRepository;
import com.example.passwordgeneration.repository.UserRepository;
import com.example.passwordgeneration.service.PasswordService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service
@AllArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final RestTemplate restTemplate;
    private final PasswordRepository repository;
    private final UserRepository userRepository;
    @Override
    public List<Password> getAllPasswords() {
        return repository.findAll();
    }

    @Override
    public Password getPasswordById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Password wasn't found"));
    }

    @SneakyThrows
    @Override
    public PasswordResponse generatePass(int length, boolean excludeNumbers,
                                         boolean excludeSpecialChars) {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new
                FileInputStream("D:\\Java\\tests\\PasswordGeneration2\\src\\main\\resources\\apikey.properties");
        properties.load(fileInputStream);
        String apiKey = properties.getProperty("apiKey");
        String url = "https://api.api-ninjas.com/v1/passwordgenerator?length=" + length +
                "&excludeNumbers=" + excludeNumbers + "&excludeSpecialChars=" + excludeSpecialChars +
                "&X-Api-Key=" + apiKey;
        String jsonStr = restTemplate.getForObject(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonStr);
        return new PasswordResponse(root.path("random_password").asText());
    }
    @Override
    public Password createPass(int length, boolean excludeNumbers, boolean excludeSpecialChars){
        Password password = new Password(length, excludeNumbers, excludeSpecialChars,
                generatePass(length, excludeNumbers, excludeSpecialChars).getRandomPassword());
        Password existPassword = repository.findByRandomPassword(password.getRandomPassword());
        if(existPassword == null) {
            return repository.save(password);
        }
        return existPassword;
    }

    @Override
    public Password updatePassword(@PathVariable Long id, PasswordRequest passwordRequest) {
        Optional<Password> existPassword = repository.findById(id);
        if (existPassword.isEmpty()) {
            return null;
        }
        Password password = new Password(passwordRequest.getLength(),
                passwordRequest.isExcludeNumbers(),
                passwordRequest.isExcludeSpecialChars(),
                createPass(passwordRequest.getLength(),
                        passwordRequest.isExcludeNumbers(),
                        passwordRequest.isExcludeSpecialChars()).getRandomPassword());
        password.setId(id);
        return repository.save(password);
    }
    @Override
    public boolean deletePassword(Long id) {
        Optional<Password> existPassword = repository.findById(id);
        if(existPassword.isEmpty()){
            return false;
        }
        List<User> users = userRepository.findAll();
        for(User user : users){
            if(user.getPassword() != null && user.getPassword().equals(existPassword.get())){
                user.setPassword(null);
            }
        }
        repository.delete(existPassword.get());
        return true;
    }
}
