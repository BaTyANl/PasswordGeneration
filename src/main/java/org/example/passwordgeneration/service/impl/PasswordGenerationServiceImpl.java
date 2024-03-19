package org.example.passwordgeneration.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.passwordgeneration.dto.PasswordResponse;
import org.example.passwordgeneration.service.PasswordGenerationService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.util.Properties;
@Service
@AllArgsConstructor
public class PasswordGenerationServiceImpl implements PasswordGenerationService {
    private final RestTemplate restTemplate;
    @Override
    @SneakyThrows
    public PasswordResponse createPass(int length, boolean excludeNumbers,
                                       boolean excludeSpecialChars) {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("D:\\Java\\PasswordGeneration\\src\\main\\resources\\application.properties");
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
}
