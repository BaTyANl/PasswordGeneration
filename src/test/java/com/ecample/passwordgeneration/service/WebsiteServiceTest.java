package com.ecample.passwordgeneration.service;

import com.example.passwordgeneration.cache.InMemoryCache;
import com.example.passwordgeneration.dto.request.UserRequest;
import com.example.passwordgeneration.dto.request.WebsiteRequest;
import com.example.passwordgeneration.dto.response.WebsiteResponse;
import com.example.passwordgeneration.model.Password;
import com.example.passwordgeneration.model.User;
import com.example.passwordgeneration.model.Website;
import com.example.passwordgeneration.repository.PasswordRepository;
import com.example.passwordgeneration.repository.UserRepository;
import com.example.passwordgeneration.repository.WebsiteRepository;
import com.example.passwordgeneration.service.PasswordService;
import com.example.passwordgeneration.service.WebsiteService;
import com.example.passwordgeneration.service.impl.WebsiteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WebsiteServiceTest {
    public static final String WEBSITE_KEY = "Website";

    private WebsiteService websiteService;

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
        websiteService = new WebsiteServiceImpl(
                websiteRepository, userRepository, passwordService, passwordRepository, cache);
    }

    @Test
    void getAllWebsitesTest(){
        Website website1 = new Website();
        website1.setId(1L);
        website1.setWebsiteName("website1");
        Set<User> users1 = new HashSet<>();
        users1.add(new User());
        website1.setUsers(users1);

        Website website2 = new Website();
        website2.setId(2L);
        website2.setWebsiteName("website2");
        Set<User> users2 = new HashSet<>();
        users2.add(new User());
        website1.setUsers(users2);

        List<Website> websites = List.of(website1, website2);

        when(websiteRepository.findAll()).thenReturn(websites);

        List<WebsiteResponse> websiteResponses = websiteService.getAllWebsites();

        assertEquals(2, websiteResponses.size());

        WebsiteResponse response1 = websiteResponses.get(0);
        assertEquals(website1.getId(), response1.getId());
        assertEquals(website1.getWebsiteName(), response1.getWebsiteName());
        Set<String> expectedUsernames1 = website1.getUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());
        assertEquals(expectedUsernames1, response1.getUsers());

        WebsiteResponse response2 = websiteResponses.get(1);
        assertEquals(website2.getId(), response2.getId());
        assertEquals(website2.getWebsiteName(), response2.getWebsiteName());
        Set<String> expectedUsernames2 = website2.getUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());
        assertEquals(expectedUsernames2, response2.getUsers());
        verify(websiteRepository, times(1)).findAll();
    }

    @Test
    void testGetWebsiteById_ExistsInCache() {
        Long id = 1L;
        Website website = new Website();
        website.setId(id);
        website.setWebsiteName("website1");
        Set<User> users1 = new HashSet<>();
        users1.add(new User());
        website.setUsers(users1);

        when(cache.get(WEBSITE_KEY + id)).thenReturn(website);

        WebsiteResponse response = websiteService.getWebsiteById(id);

        assertEquals(id, response.getId());
        assertEquals(website.getWebsiteName(), response.getWebsiteName());
        assertEquals(website.getUsers().stream().map(User::getUsername).collect(Collectors.toSet()), response.getUsers());

        verify(cache, times(1)).get(WEBSITE_KEY + id);
        verifyNoInteractions(userRepository);
        verify(cache, times(1)).put(WEBSITE_KEY + id, website);
    }

    @Test
    void testGetWebsiteById() {
        Long id = 1L;
        Website website = new Website();
        website.setId(id);
        website.setWebsiteName("website1");
        Set<User> users1 = new HashSet<>();
        users1.add(new User());
        website.setUsers(users1);

        when(cache.get(WEBSITE_KEY + id)).thenReturn(null);
        when(websiteRepository.findById(id)).thenReturn(Optional.of(website));

        WebsiteResponse response = websiteService.getWebsiteById(id);

        assertEquals(id, response.getId());
        assertEquals(website.getWebsiteName(), response.getWebsiteName());
        assertEquals(website.getUsers().stream().map(User::getUsername).collect(Collectors.toSet()), response.getUsers());

        verify(cache, times(1)).get(WEBSITE_KEY + id);
        verify(websiteRepository, times(1)).findById(id);
        verify(cache, times(1)).put(WEBSITE_KEY + id, website);
    }

    @Test
    void testGetWebsiteById_WhenWebsiteDoesNotExist() {
        Long id = 1L;

        when(cache.get(WEBSITE_KEY + id)).thenReturn(null);
        when(websiteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> websiteService.getWebsiteById(id));

        verify(cache, times(1)).get(WEBSITE_KEY + id);
        verify(websiteRepository, times(1)).findById(id);
        verifyNoMoreInteractions(cache, userRepository);
    }

    @Test
    void testCreateWebsite_NewWebsite() {
        Long id = 1L;
        Website website = new Website();
        website.setId(id);
        website.setWebsiteName("website");
        Set<User> users1 = new HashSet<>();
        users1.add(new User());
        website.setUsers(users1);

        UserRequest userRequest1 = new UserRequest("user1",
                9, false, true);
        UserRequest userRequest2 = new UserRequest("user2",
                9, false, true);

        WebsiteRequest websiteRequest = new WebsiteRequest("website",
                Arrays.asList(userRequest1, userRequest2));

        when(websiteRepository.findByWebsiteName("website")).thenReturn(null);

        User existUser1 = new User("user1", null);
        User existUser2 = new User("user2", null);

        when(userRepository.findByUsername("user1")).thenReturn(null);
        when(userRepository.findByUsername("user2")).thenReturn(null);

        when(passwordService.generatePass(anyInt(), anyBoolean(), anyBoolean())).thenReturn("qwerty123");

        Password password = new Password(9, false, true, "qwerty123");
        when(passwordRepository.findByRandomPassword("qwerty123")).thenReturn(null);
        when(passwordRepository.save(any(Password.class))).thenReturn(password);

        when(userRepository.save(any(User.class))).thenReturn(existUser1, existUser2);

        when(websiteRepository.save(any(Website.class))).thenReturn(website);

        WebsiteResponse response = websiteService.createWebsite(websiteRequest);

        assertEquals("website", response.getWebsiteName());
        assertTrue(response.getUsers().contains("user1"));
        assertTrue(response.getUsers().contains("user2"));

        verify(websiteRepository, times(1)).findByWebsiteName("website");
        verify(userRepository, times(1)).findByUsername("user1");
        verify(userRepository, times(1)).findByUsername("user2");
        verify(passwordService, times(2)).generatePass(anyInt(), anyBoolean(), anyBoolean());
        verify(passwordRepository, times(2)).findByRandomPassword("qwerty123");
        verify(passwordRepository, times(2)).save(any(Password.class));
        verify(userRepository, times(2)).save(any(User.class));
        verify(websiteRepository, times(1)).save(any(Website.class));
        verify(cache, times(2)).put(eq("User" + existUser1.getId()), any(User.class));
        verify(cache, times(2)).put(eq("User" + existUser2.getId()), any(User.class));
        verify(cache, times(2)).put(eq("Password" + password.getId()), any(Password.class));
    }

    @Test
    void testCreateWebsite() {
        UserRequest userRequest1 = new UserRequest("user1",
                9, false, true);
        UserRequest userRequest2 = new UserRequest("user2",
                9, false, true);
        WebsiteRequest websiteRequest = new WebsiteRequest("website",
                Arrays.asList(userRequest1, userRequest2));

        when(websiteRepository.findByWebsiteName("website")).thenReturn(new Website());

        assertThrows(ConcurrentModificationException.class, () -> websiteService.createWebsite(websiteRequest));

        verify(websiteRepository, times(1)).findByWebsiteName("website");
        verifyNoMoreInteractions(userRepository, passwordService, passwordRepository, cache);
    }

    @Test
    void testAddUser_WebsiteExistsAndUserDoesNotExist() {
        Long id = 1L;
        Website existWebsite = new Website();
        existWebsite.setId(id);
        existWebsite.setWebsiteName("website");
        Set<User> users1 = new HashSet<>();
        users1.add(new User());
        existWebsite.setUsers(users1);

        when(cache.get(WEBSITE_KEY + id)).thenReturn(existWebsite);
        when(userRepository.findByUsername("user1")).thenReturn(null);

        UserRequest userRequest = new UserRequest("user1",
                9, false, true);

        when(passwordService.generatePass(anyInt(), anyBoolean(), anyBoolean())).thenReturn("qwerty123");

        Password password = new Password(9, false, true, "qwerty123");
        when(passwordRepository.findByRandomPassword("qwerty123")).thenReturn(null);
        when(passwordRepository.save(any(Password.class))).thenReturn(password);

        User user = new User("user1", password);
        when(userRepository.save(any(User.class))).thenReturn(user);

        WebsiteResponse response = websiteService.addUser(id, userRequest);

        assertEquals(existWebsite.getId(), response.getId());
        assertEquals("website", response.getWebsiteName());
        assertTrue(response.getUsers().contains("user1"));

        verify(cache, times(1)).get(WEBSITE_KEY + id);
        verify(userRepository, times(1)).findByUsername("user1");
        verify(passwordService, times(1)).generatePass(anyInt(), anyBoolean(), anyBoolean());
        verify(passwordRepository, times(1)).findByRandomPassword("qwerty123");
        verify(passwordRepository, times(1)).save(any(Password.class));
        verify(userRepository, times(1)).save(any(User.class));
        verify(cache, times(1)).put(eq(WEBSITE_KEY + existWebsite.getId()), any(Website.class));
        verify(cache, times(1)).put(eq("User" + user.getId()), any(User.class));
    }

    @Test
    void testAddUser_WebsiteExistsAndUserExistsInWebsite() {
        Long id = 1L;
        Website existWebsite = new Website();
        existWebsite.setId(id);
        existWebsite.setWebsiteName("website");
        Set<User> users1 = new HashSet<>();
        users1.add(new User());
        existWebsite.setUsers(users1);

        when(cache.get(WEBSITE_KEY + id)).thenReturn(existWebsite);

        UserRequest userRequest = new UserRequest("user1",
                9, false, true);

        when(websiteRepository.findInSetByUsername("user1", id)).thenReturn(new User());

        assertThrows(ConcurrentModificationException.class, () -> websiteService.addUser(id, userRequest));

        verify(cache).get(WEBSITE_KEY + id);
        verify(websiteRepository).findInSetByUsername("user1", id);
        //verifyNoMoreInteractions(userRepository, passwordService, passwordRepository, cache);
    }

    @Test
    void testAddUser_WhenWebsiteDoesNotExist() {
        when(cache.get(WEBSITE_KEY + 1L)).thenReturn(null);
        when(websiteRepository.findById(1L)).thenReturn(Optional.empty());

        UserRequest userRequest = new UserRequest("user1",
                9, false, true);

        assertThrows(NoSuchElementException.class,
                () -> websiteService.addUser(1L, userRequest)
        );

        verify(cache).get(WEBSITE_KEY + 1L);
        verify(websiteRepository).findById(1L);
        verifyNoMoreInteractions(userRepository, passwordService, passwordRepository, cache);
    }

    @Test
    void testRemoveUser_WebsiteExistsAndUserExists() {
        Long id = 1L;
        Website existWebsite = new Website();
        existWebsite.setId(id);
        existWebsite.setWebsiteName("website");
        Set<User> users1 = new HashSet<>();
        users1.add(new User());
        existWebsite.setUsers(users1);

        User existUser = new User("user1", null);

        when(cache.get(WEBSITE_KEY + id)).thenReturn(existWebsite);
        when(websiteRepository.findInSetByUsername("user1", id)).thenReturn(existUser);

        WebsiteResponse response = websiteService.removeUser(id, "user1");

        assertEquals(existWebsite.getId(), response.getId());
        assertEquals("website", response.getWebsiteName());
        assertFalse(response.getUsers().contains("user1"));

        verify(cache).get(WEBSITE_KEY + id);
        verify(websiteRepository).findInSetByUsername("user1", id);
        verify(websiteRepository).save(existWebsite);
        verify(cache).put(eq(WEBSITE_KEY + existWebsite.getId()), any(Website.class));
    }

    @Test
    void testRemoveUser_WebsiteExistsAndUserDoesNotExist() {
        Long id = 1L;
        Website existWebsite = new Website();
        existWebsite.setId(id);
        existWebsite.setWebsiteName("website");
        Set<User> users1 = new HashSet<>();
        users1.add(new User());
        existWebsite.setUsers(users1);

        when(cache.get(WEBSITE_KEY + id)).thenReturn(existWebsite);
        when(websiteRepository.findInSetByUsername("user1", id)).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> websiteService.removeUser(id, "user1"));

        verify(cache).get(WEBSITE_KEY + id);
        verify(websiteRepository).findInSetByUsername("user1", id);
        verifyNoMoreInteractions(websiteRepository, cache);
    }

    @Test
    void testRemoveUser_WebsiteDoesNotExist() {
        when(cache.get(WEBSITE_KEY + 1L)).thenReturn(null);
        when(websiteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> websiteService.removeUser(1L, "user1"));

        verify(cache).get(WEBSITE_KEY + 1L);
        verify(websiteRepository).findById(1L);
    }

    @Test
    void testDeleteWebsite() {
        Long id = 1L;
        Website existWebsite = new Website();
        existWebsite.setId(id);

        when(cache.get(WEBSITE_KEY + id)).thenReturn(existWebsite);

        websiteService.deleteWebsite(id);

        verify(cache).get(WEBSITE_KEY + id);
        verify(websiteRepository).delete(existWebsite);
    }

    @Test
    void testDeleteWebsite_WhenWebsiteDoesNotExist() {
        when(cache.get(WEBSITE_KEY + 1L)).thenReturn(null);
        when(websiteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> websiteService.deleteWebsite(1L));

        verify(cache).get(WEBSITE_KEY + 1L);
        verify(websiteRepository).findById(1L);
    }
}
