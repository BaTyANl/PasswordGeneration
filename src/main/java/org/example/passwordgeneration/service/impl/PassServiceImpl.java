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
    public String createPass(int length, boolean exclude_numbers, boolean exclude_special_chars) {
        String url = "https://api.api-ninjas.com/v1/passwordgenerator?length=" + length +
                "&exclude_numbers=" + exclude_numbers + "&exclude_special_chars=" + exclude_special_chars +
                "&X-Api-Key=YzshKnOgHb7dJFpZryvidg==OHVoTBkcQJWBnRqB";
        return restTemplate.getForObject(url, String.class);
    }
}
