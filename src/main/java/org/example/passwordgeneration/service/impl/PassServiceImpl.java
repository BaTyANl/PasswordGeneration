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
    public String createPass(int length, boolean includeUpper, boolean includeLower,
                             boolean includeSpecial, boolean includeNum) {
        String url = "https://api.genratr.com/?length=" + length +
                "&uppercase=" + includeUpper + "&lowercase=" + includeLower +
                "&special=" + includeSpecial + "&numbers=" + includeNum;
        return restTemplate.getForObject(url, String.class);
    }
}
