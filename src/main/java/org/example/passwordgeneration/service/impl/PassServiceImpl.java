package org.example.passwordgeneration.service.impl;


import lombok.AllArgsConstructor;
import org.example.passwordgeneration.service.PassService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class PassServiceImpl implements PassService {
    private final RestTemplate restTemplate;
    @Override
    public String createPass(int length, boolean excludeNumbers, boolean excludeSpecialChars) {
        String url = "https://api.api-ninjas.com/v1/passwordgenerator?length=" + length +
                "&exclude_numbers=" + excludeNumbers + "&exclude_special_chars=" + excludeSpecialChars +
                "&X-Api-Key=YzshKnOgHb7dJFpZryvidg==OHVoTBkcQJWBnRqB";
        return restTemplate.getForObject(url, String.class);
    }
}
