package com.ecample.passwordgeneration.service;

import com.example.passwordgeneration.cache.InMemoryCache;
import com.example.passwordgeneration.dto.request.PasswordRequest;
import com.example.passwordgeneration.dto.response.PasswordResponse;
import com.example.passwordgeneration.repository.PasswordRepository;
import com.example.passwordgeneration.repository.UserRepository;
import com.example.passwordgeneration.service.PasswordService;
import com.example.passwordgeneration.service.impl.PasswordServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class PasswordServiceTest {
    private PasswordService passwordService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PasswordRepository passwordRepository;

    @Mock
    private InMemoryCache cache;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        passwordService = new PasswordServiceImpl(restTemplate,
                passwordRepository, userRepository, cache);
    }
}
