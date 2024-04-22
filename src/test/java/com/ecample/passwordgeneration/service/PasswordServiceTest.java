package com.ecample.passwordgeneration.service;

import com.example.passwordgeneration.cache.InMemoryCache;
import com.example.passwordgeneration.dto.request.PasswordRequest;
import com.example.passwordgeneration.dto.response.PasswordResponse;
import com.example.passwordgeneration.model.Password;
import com.example.passwordgeneration.model.User;
import com.example.passwordgeneration.repository.PasswordRepository;
import com.example.passwordgeneration.repository.UserRepository;
import com.example.passwordgeneration.service.PasswordService;
import com.example.passwordgeneration.service.impl.PasswordServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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

    @Test
    void testGetPasswordById() {
        Long id = 1L;
        Password password = new Password();
        password.setId(id);
        password.setLength(20);
        password.setExcludeNumbers(true);
        password.setExcludeSpecialChars(true);
        password.setRandomPassword("yVjcTRjXnPfpiRuHTZeT");
        Set<User> users = new HashSet<>();
        users.add(new User());
        password.setUsers(users);
        when(passwordRepository.findById(id)).thenReturn(Optional.of(password));

        PasswordResponse result = passwordService.getPasswordById(id);

        verify(passwordRepository, times(1)).findById(id);
        assertNotEquals(result, new PasswordResponse(password.getId(), result.getRandomPassword()));
    }

    @Test
    void testGetPasswordById_PasswordNotFound() {
        Long id = 1L;
        when(passwordRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> passwordService.getPasswordById(id));
    }

    @Test
    void testGetAllPasswords(){
        Password password1 = new Password();
        password1.setId(1L);
        password1.setLength(20);
        password1.setExcludeNumbers(true);
        password1.setExcludeSpecialChars(true);
        password1.setRandomPassword("yVjcTRjXnPfpiRuHTZeT");
        Set<User> users1 = new HashSet<>();
        users1.add(new User());
        password1.setUsers(users1);

        Password password2 = new Password();
        password2.setId(2L);
        password2.setLength(20);
        password2.setExcludeNumbers(false);
        password2.setExcludeSpecialChars(false);
        password2.setRandomPassword("uDx5AAsdl)O1KMn!DItl");
        Set<User> users2 = new HashSet<>();
        users2.add(new User());
        password2.setUsers(users2);

        List<Password> passwords = List.of(password1, password2);
        when(passwordRepository.findAll()).thenReturn(passwords);

        List<PasswordResponse> result = passwordService.getAllPasswords();

        assertEquals(2, result.size());
        assertFalse(result.contains(new PasswordResponse(
                password1.getId(), password1.getRandomPassword())));
        assertFalse(result.contains(new PasswordResponse(
                password2.getId(), password2.getRandomPassword())));
        verify(passwordRepository, times(1)).findAll();
    }

    /*
    @Test
    void testCreatePass(){
        PasswordResponse passwordResponse =
                new PasswordResponse(1L, "yVjcTRjXnPfpiRuHTZeT");
        when(restTemplate.getForObject(anyString(), eq(PasswordResponse.class)))
                .thenReturn(passwordResponse);

        PasswordResponse result = passwordService.createPass(20, true, true);
        assertNotEquals(new );
    }
    */

    /*@Test
    void testUpdatePassword(){

    }*/

    @Test
    void testDeletePassword() {
        Long id = 1L;
        Password password = new Password();
        password.setId(id);
        password.setLength(20);
        password.setExcludeNumbers(true);
        password.setExcludeSpecialChars(true);
        password.setRandomPassword("yVjcTRjXnPfpiRuHTZeT");
        Set<User> users = new HashSet<>();
        users.add(new User());
        password.setUsers(users);

        when(passwordRepository.findById(id)).thenReturn(Optional.of(password));

        passwordService.deletePassword(id);

        verify(passwordRepository, times(1)).findById(id);
        verify(passwordRepository, times(1)).delete(password);
    }
}

