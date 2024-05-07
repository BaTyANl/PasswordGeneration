package com.ecample.passwordgeneration.service;

import com.example.passwordgeneration.cache.InMemoryCache;
import com.example.passwordgeneration.dto.request.PasswordRequest;
import com.example.passwordgeneration.dto.response.PasswordResponse;
import com.example.passwordgeneration.model.Password;
import com.example.passwordgeneration.model.User;
import com.example.passwordgeneration.model.Website;
import com.example.passwordgeneration.repository.PasswordRepository;
import com.example.passwordgeneration.repository.UserRepository;
import com.example.passwordgeneration.service.PasswordService;
import com.example.passwordgeneration.service.impl.PasswordServiceImpl;
import com.example.passwordgeneration.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PasswordServiceTest {
    public static final String PASSWORD_KEY = "Password";
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
    void testGetPasswordById_FromCache() {
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
        when(cache.get(PASSWORD_KEY + id)).thenReturn(password);

        PasswordResponse result = passwordService.getPasswordById(id);

        assertEquals(password.getId(), result.getId());
        assertEquals(password.getRandomPassword(), result.getRandomPassword());
        verify(passwordRepository, times(0)).findById(id);
        verify(cache, times(1)).put(anyString(), any(Password.class));
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

        when(cache.get(PASSWORD_KEY + id)).thenReturn(null);
        when(passwordRepository.findById(id)).thenReturn(Optional.of(password));

        PasswordResponse result = passwordService.getPasswordById(id);

        assertEquals(password.getId(), result.getId());
        assertEquals(password.getRandomPassword(), result.getRandomPassword());
        verify(passwordRepository, times(1)).findById(id);
        verify(cache, times(1)).put(PASSWORD_KEY + id, password);

    }

    @Test
    void testGetPasswordById_PasswordNotFound() {
        Long id = 1L;
        when(cache.get(PASSWORD_KEY + id)).thenReturn(null);
        when(passwordRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> passwordService.getPasswordById(id));
        verify(passwordRepository, times(1)).findById(id);
        verify(cache, times(0)).put(anyString(), any(Password.class));
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

    @Test
    void testCreatePass_newPassword(){
        Password password = new Password();
        int length = 11;
        boolean excludeNumbers = false;
        boolean excludeSpecialChars = true;
        String randomPassword = "qwerty123";
        Set<User> users = new HashSet<>();
        users.add(new User());

        when(passwordRepository.findByRandomPassword(randomPassword)).thenReturn(null);
        password.setId(1L);
        password.setLength(length);
        password.setExcludeSpecialChars(excludeSpecialChars);
        password.setRandomPassword(randomPassword);
        password.setExcludeNumbers(excludeNumbers);
        password.setUsers(users);

        PasswordResponse result = passwordService.createPass(length, excludeNumbers, excludeSpecialChars);

        assertEquals(password.getRandomPassword(), result.getRandomPassword());
    }

    @Test
    void testCreatePass(){
        Password password = new Password();
        int length = 11;
        boolean excludeNumbers = false;
        boolean excludeSpecialChars = true;
        Set<User> users = new HashSet<>();
        users.add(new User());

        when(passwordRepository.findByRandomPassword("qwerty123")).thenReturn(null);
        password.setId(1L);
        password.setLength(length);
        password.setExcludeSpecialChars(excludeSpecialChars);
        password.setRandomPassword("qwerty123");
        password.setExcludeNumbers(excludeNumbers);
        password.setUsers(users);
        when(passwordRepository.save(any(Password.class))).thenReturn(password);
        doNothing().when(cache).put(anyString(), any(Password.class));

        PasswordResponse result = passwordService.createPass(length, excludeNumbers, excludeSpecialChars);

        assertNotNull(result);
        assertEquals(password.getRandomPassword(), result.getRandomPassword());
    }


    @Test
    void testUpdatePassword_FromCache(){
        Long id = 1L;
        Password password = new Password();
        password.setId(id);
        password.setLength(7);
        password.setExcludeNumbers(false);
        password.setExcludeSpecialChars(true);
        password.setRandomPassword("qwerty123");
        Set<User> users = new HashSet<>();
        users.add(new User());
        password.setUsers(users);

        when(cache.get(PASSWORD_KEY + id)).thenReturn(password);

        when(passwordService.generatePass(7, false, true))
                .thenReturn("qwerty123");

        when(passwordRepository.save(password)).thenReturn(password);
        doNothing().when(cache).put(anyString(), any(Password.class));

        PasswordResponse result = passwordService.updatePassword(id,
                new PasswordRequest(20, true, true));

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(password.getRandomPassword(), result.getRandomPassword());
    }

    @Test
    void testUpdatePassword(){
        Long id = 1L;
        Password password = new Password();
        password.setId(id);
        password.setLength(9);
        password.setExcludeNumbers(false);
        password.setExcludeSpecialChars(true);
        password.setRandomPassword("qwert123");
        Set<User> users = new HashSet<>();
        users.add(new User());
        password.setUsers(users);

        PasswordRequest passwordRequest = new PasswordRequest(9, false, true);
        when(cache.get(PASSWORD_KEY + id)).thenReturn(null);

        when(passwordRepository.findById(id)).thenReturn(Optional.of(password));

        when(passwordRepository.save(password)).thenReturn(password);

        doNothing().when(cache).put(anyString(), any(Password.class));

        PasswordResponse result = passwordService.updatePassword(id, passwordRequest);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(password.getRandomPassword(), result.getRandomPassword());
    }

    @Test
    void updatePassword_PasswordNotFound() {
        Long id = 1L;
        PasswordRequest passwordRequest = new PasswordRequest(10, true, true);
        when(cache.get(PASSWORD_KEY + id)).thenReturn(null);
        when(passwordRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> passwordService.updatePassword(id, passwordRequest));
    }

    @Test
    void testDeletePassword_FromCache() {
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

        when(cache.get(PASSWORD_KEY + id)).thenReturn(password);
        List<User> users1 = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword(password);
        Set<Website> websites1 = new HashSet<>();
        websites1.add(new Website());
        user1.setWebsite(websites1);

        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("user1");
        user2.setPassword(password);
        Set<Website> websites2 = new HashSet<>();
        websites2.add(new Website());
        user2.setWebsite(websites2);

        users1.add(user1);
        users1.add(user2);

        when(userRepository.findAll()).thenReturn(users1);

        when(userRepository.save(user1)).thenReturn(user1);
        when(userRepository.save(user2)).thenReturn(user2);

        doNothing().when(cache).remove(anyString());

        doNothing().when(passwordRepository).delete(password);

        passwordService.deletePassword(id);

        for(User user : users1){
            assertNull(user.getPassword());
            verify(cache, times(2))
                    .remove(UserServiceImpl.USER_KEY + user.getId());
        }
        verify(cache, times(1)).remove(PASSWORD_KEY + id);
        verify(passwordRepository, times(1)).delete(password);
    }

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

        when(cache.get(PASSWORD_KEY + id)).thenReturn(null);
        when(passwordRepository.findById(id)).thenReturn(Optional.of(password));

        List<User> users1 = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword(password);
        Set<Website> websites1 = new HashSet<>();
        websites1.add(new Website());
        user1.setWebsite(websites1);

        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("user1");
        user2.setPassword(password);
        Set<Website> websites2 = new HashSet<>();
        websites2.add(new Website());
        user2.setWebsite(websites2);

        users1.add(user1);
        users1.add(user2);

        when(userRepository.findAll()).thenReturn(users1);

        when(userRepository.save(user1)).thenReturn(user1);
        when(userRepository.save(user2)).thenReturn(user2);

        doNothing().when(cache).remove(anyString());

        doNothing().when(passwordRepository).delete(password);

        passwordService.deletePassword(id);

        for(User user : users1){
            assertNull(user.getPassword());
            verify(cache, times(2))
                    .remove(UserServiceImpl.USER_KEY + user.getId());
        }
        verify(cache, times(1)).remove(PASSWORD_KEY + id);
        verify(passwordRepository, times(1)).delete(password);
    }

    @Test
    void deletePassword_PasswordNotFound() {
        Long id = 1L;
        when(cache.get(PASSWORD_KEY + id)).thenReturn(null);
        when(passwordRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> passwordService.deletePassword(id));
    }
}