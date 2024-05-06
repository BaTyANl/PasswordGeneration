package com.ecample.passwordgeneration.service;

import com.example.passwordgeneration.cache.InMemoryCache;
import com.example.passwordgeneration.dto.request.UserRequest;
import com.example.passwordgeneration.dto.response.UserResponse;
import com.example.passwordgeneration.model.Password;
import com.example.passwordgeneration.model.User;
import com.example.passwordgeneration.model.Website;
import com.example.passwordgeneration.repository.PasswordRepository;
import com.example.passwordgeneration.repository.UserRepository;
import com.example.passwordgeneration.repository.WebsiteRepository;
import com.example.passwordgeneration.service.PasswordService;
import com.example.passwordgeneration.service.UserService;
import com.example.passwordgeneration.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    public static final String USER_KEY = "User";

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InMemoryCache cache;

    @Mock
    private WebsiteRepository websiteRepository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private PasswordRepository passwordRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository,
                passwordService, passwordRepository, websiteRepository, cache);
    }

    @Test
    void testGetAllUsers(){
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword(new Password());
        Set<Website> websites1 = new HashSet<>();
        websites1.add(new Website());
        user1.setWebsite(websites1);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setPassword(new Password());
        Set<Website> websites2 = new HashSet<>();
        websites2.add(new Website());
        user2.setWebsite(websites2);

        List<User> users = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<UserResponse> result = userService.getAllUsers();
        assertEquals(2, result.size());

        UserResponse response1 = result.get(0);
        assertEquals(1L, response1.getId());
        assertEquals(1, response1.getWebsites().size());
        assertEquals("user1", response1.getUsername());
        assertNull(response1.getPassword());

        UserResponse response2 = result.get(1);
        assertEquals(2L, response2.getId());
        assertEquals(1, response2.getWebsites().size());
        assertEquals("user2", response2.getUsername());
        assertNull(response2.getPassword());

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testGetFromRepo_UserExistsInCache() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);

        when(cache.get(USER_KEY + userId)).thenReturn(existingUser);

        User result = userService.getFromRepo(userId);

        verify(cache, times(1)).get(USER_KEY + userId);
        verify(userRepository, times(0)).findById(userId);
        verifyNoMoreInteractions(cache, userRepository);

        assertEquals(existingUser, result);
    }

    @Test
    void testGetFromRepo_UserExistsInRepository() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);

        when(cache.get(USER_KEY + userId)).thenReturn(null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        User result = userService.getFromRepo(userId);

        verify(cache, times(1)).get(USER_KEY + userId);
        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(cache, userRepository);

        assertEquals(existingUser, result);
    }

    @Test
    void testGetFromRepo_UserNotFound() {
        Long userId = 1L;

        when(cache.get(USER_KEY + userId)).thenReturn(null);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> userService.getFromRepo(userId));

        verify(cache, times(1)).get(USER_KEY + userId);
        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(cache, userRepository);
    }

    @Test
    void getUserByIdTest_NotInCache(){
        Long userId = 1L;
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setPassword(new Password());
        Set<Website> websites1 = new HashSet<>();
        websites1.add(new Website());
        user.setWebsite(websites1);

        when(cache.get(USER_KEY + userId)).thenReturn(null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponse result = userService.getUserById(userId);

        assertEquals(userId, result.getId());
        assertEquals(1, result.getWebsites().size());
        assertEquals("user1", result.getUsername());
        assertNull(result.getPassword());

        verify(cache, times(1)).get(USER_KEY + userId);
        verify(userRepository, times(1)).findById(userId);
        verify(cache, times(1)).put(USER_KEY + userId, user);
    }

    @Test
    void getUserByIdTest(){
        Long userId = 1L;
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setPassword(new Password());
        Set<Website> websites1 = new HashSet<>();
        websites1.add(new Website());
        user.setWebsite(websites1);

        when(cache.get(USER_KEY + userId)).thenReturn(user);

        UserResponse result = userService.getUserById(userId);

        assertEquals(userId, result.getId());
        assertEquals(1, result.getWebsites().size());
        assertEquals("user1", result.getUsername());
        assertNull(result.getPassword());

        verify(cache, times(1)).get(USER_KEY + userId);
        verifyNoInteractions(userRepository);
        verify(cache, times(1)).put(USER_KEY + userId, user);
    }

    @Test
    void testGetUserById_userNotFound() {
        Long userId = 1L;
        when(cache.get(USER_KEY + userId)).thenReturn(null);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> userService.getUserById(userId));

        verify(cache, times(1)).get(USER_KEY + userId);
        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository, cache);
    }

    @Test
    void createUserTest_newUser(){
        UserRequest userRequest = new UserRequest(
                "user1", 9, false, true);
        when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(null);
        when(passwordService.generatePass(userRequest.getLength(),
                userRequest.isExcludeNumbers(),
                userRequest.isExcludeSpecialChars())).thenReturn("qwerty123");
        when(passwordRepository.findByRandomPassword("qwerty123")).thenReturn(null);
        Password newPassword = new Password(userRequest.getLength(),
                userRequest.isExcludeNumbers(), userRequest.isExcludeSpecialChars(), "qwerty123");
        when(passwordRepository.save(any(Password.class))).thenReturn(newPassword);

        UserResponse result = userService.createUser(userRequest);

        assertEquals(0, result.getWebsites().size());
        assertEquals("user1", result.getUsername());
        assertEquals("qwerty123", result.getPassword());

        verify(userRepository, times(1)).findByUsername(userRequest.getUsername());
        verify(passwordService, times(1)).generatePass(
                userRequest.getLength(), userRequest.isExcludeNumbers(), userRequest.isExcludeSpecialChars());
        verify(passwordRepository, times(1)).findByRandomPassword("qwerty123");
    }

    @Test
    void testCreateUser_userExists() {
        UserRequest userRequest = new UserRequest("user1", 9, false, true);
        User existingUser = new User("user1", new Password(9, false, true, "qwerty123"));
        when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(existingUser);

        assertThrows(ConcurrentModificationException.class,
                () -> userService.createUser(userRequest));

        verify(userRepository, times(1)).findByUsername(userRequest.getUsername());
        verifyNoInteractions(passwordService, passwordRepository, cache);
    }

    @Test
    void createManyUsersTest() {
        List<UserRequest> userRequests = Arrays.asList(
                new UserRequest("username1", 8, true, true),
                new UserRequest("username2", 10, false, true)
        );
        when(userRepository.findByUsername("username1")).thenReturn(null);
        when(userRepository.findByUsername("username2")).thenReturn(null);
        when(passwordService.generatePass(8, true, true)).thenReturn("password1");
        when(passwordService.generatePass(10, false, true)).thenReturn("password2");
        when(passwordRepository.findByRandomPassword("password1")).thenReturn(null);
        when(passwordRepository.findByRandomPassword("password2")).thenReturn(null);
        Password generatedPassword1 = new Password(8, true, true, "password1");
        Password generatedPassword2 = new Password(10, false, true, "password2");
        when(passwordRepository.save(any(Password.class)))
                .thenReturn(generatedPassword1)
                .thenReturn(generatedPassword2);

        List<UserResponse> results = userService.createManyUsers(userRequests);

        assertEquals(2, results.size());
        UserResponse result1 = results.get(0);
        assertEquals(0, result1.getWebsites().size());
        assertEquals("username1", result1.getUsername());
        assertEquals("password1", result1.getPassword());

        UserResponse result2 = results.get(1);
        assertEquals(0, result2.getWebsites().size());
        assertEquals("username2", result2.getUsername());
        assertEquals("password2", result2.getPassword());

        verify(userRepository, times(2)).findByUsername(anyString());
        verify(passwordService, times(1)).generatePass(8, true, true);
        verify(passwordService, times(1)).generatePass(10, false, true);
        verify(passwordRepository, times(1)).findByRandomPassword("password1");
        verify(passwordRepository, times(1)).findByRandomPassword("password2");
        verify(passwordRepository, times(2)).save(any(Password.class));
        verify(cache, times(2)).put(anyString(), any(User.class));
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void updateUserTest_userExists(){
        Long userId = 1L;
        UserRequest userRequest = new UserRequest("newUsername", 8, true, true);

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");
        Password existingPassword = new Password(8, true, true, "oldPassword");
        existingUser.setPassword(existingPassword);

        when(cache.get(USER_KEY + userId)).thenReturn(existingUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(null);
        when(passwordService.generatePass(userRequest.getLength(),
                userRequest.isExcludeNumbers(),
                userRequest.isExcludeSpecialChars())).thenReturn("newPassword");

        UserResponse result = userService.updateUser(userId, userRequest);

        assertEquals(userId, result.getId());
        assertEquals(0, result.getWebsites().size());
        assertEquals("newUsername", result.getUsername());
        assertEquals("newPassword", result.getPassword());

        assertEquals("newUsername", existingUser.getUsername());
        assertEquals("newPassword", existingUser.getPassword().getRandomPassword());

        verify(cache, times(1)).get(USER_KEY + userId);
        verify(userRepository, times(0)).findById(userId);
        verify(userRepository, times(1)).findByUsername(userRequest.getUsername());
        verify(passwordService, times(1)).generatePass(userRequest.getLength(),
                userRequest.isExcludeNumbers(),
                userRequest.isExcludeSpecialChars());
        verify(passwordRepository, times(0)).save(any(Password.class));
        verify(cache, times(0)).put(anyString(), any(Password.class));
        verify(userRepository, times(1)).save(existingUser);
        verify(cache, times(1)).put(USER_KEY + userId, existingUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        Long userId = 1L;
        UserRequest userRequest = new UserRequest("newUsername", 8, true, true);

        when(cache.get(USER_KEY + userId)).thenReturn(null);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.updateUser(userId, userRequest));

        verify(cache, times(1)).get(USER_KEY + userId);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(0)).findByUsername(anyString());
        verify(passwordService, times(0)).generatePass(anyInt(), anyBoolean(), anyBoolean());
        verify(passwordRepository, times(0)).save(any(Password.class));
        verify(cache, times(0)).put(anyString(), any(Password.class));
        verify(userRepository, times(0)).save(any(User.class));
        verify(cache, times(0)).put(anyString(), any(User.class));
    }

    @Test
    void testUpdateUser_ConcurrentModificationException() {
        Long userId = 1L;
        UserRequest userRequest = new UserRequest("newUsername", 8, true, true);

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");
        User existingUserWithSameUsername = new User();

        when(cache.get(USER_KEY + userId)).thenReturn(existingUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(existingUserWithSameUsername);

        assertThrows(ConcurrentModificationException.class,
                () -> userService.updateUser(userId, userRequest));

        verify(cache, times(1)).get(USER_KEY + userId);
        verify(userRepository, times(0)).findById(userId);
        verify(userRepository, times(1)).findByUsername(userRequest.getUsername());
        verify(passwordService, times(0)).generatePass(anyInt(), anyBoolean(), anyBoolean());
        verify(passwordRepository, times(0)).save(any(Password.class));
        verify(cache, times(0)).put(anyString(), any(Password.class));
        verify(userRepository, times(0)).save(any(User.class));
        verify(cache, times(0)).put(anyString(), any(User.class));
    }

    @Test
    void testDeleteUser_UserExists() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);

        when(cache.get(USER_KEY + userId)).thenReturn(existingUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        userService.deleteUser(userId);

        verify(cache, times(1)).get(USER_KEY + userId);
        verify(userRepository, times(0)).findById(userId);
        verify(websiteRepository, times(1)).findAll();
        verify(userRepository, times(1)).delete(existingUser);
        verify(cache, times(1)).remove(USER_KEY + existingUser.getId());
        verifyNoMoreInteractions(cache, userRepository, websiteRepository);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        Long userId = 1L;

        when(cache.get(USER_KEY + userId)).thenReturn(null);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> userService.deleteUser(userId));

        verify(cache, times(1)).get(USER_KEY + userId);
        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(cache, userRepository, websiteRepository);
    }
}
