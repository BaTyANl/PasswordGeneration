package com.example.passwordgeneration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Rest template config.
 */
@Configuration
public class PasswordConfig {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}

