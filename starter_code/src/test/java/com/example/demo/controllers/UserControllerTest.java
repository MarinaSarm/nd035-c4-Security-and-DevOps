package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception{
        when(encoder.encode("testpassword")).thenReturn("thisishashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testpassword");
        request.setConfirmPassword("testpassword");
        final ResponseEntity<User> response = userController.createUser(request);
        // we got response
        assertNotNull(response);
        // status success
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisishashed", user.getPassword());
    }

    @Test
    public void create_user_failed() throws Exception{
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testpassword");
        request.setConfirmPassword("otherpassword");
        final ResponseEntity<User> response = userController.createUser(request);
        // we got response
        assertNotNull(response);
        // status failed
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void get_user_by_name() throws Exception{
        when(encoder.encode("testpassword")).thenReturn("thisishashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testpassword");
        request.setConfirmPassword("testpassword");
        final ResponseEntity<User> response1 = userController.createUser(request);
        assertEquals(200, response1.getStatusCodeValue());
        User user = response1.getBody();
        assertEquals("test", user.getUsername());
        // mock user repository to obtain data
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        final ResponseEntity<User> response2 = userController.findByUserName("test");
        // we got response
        assertNotNull(response2);
        // status success
        assertEquals(200, response2.getStatusCodeValue());
        User user2 = response2.getBody();
        assertNotNull(user2);
        assertEquals(0, user2.getId());
        assertEquals("test", user2.getUsername());
        assertEquals("thisishashed", user2.getPassword());
    }

    @Test
    public void get_user_by_name_not_found() throws Exception{
        when(encoder.encode("testpassword")).thenReturn("thisishashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testpassword");
        request.setConfirmPassword("testpassword");
        final ResponseEntity<User> response1 = userController.createUser(request);
        assertEquals(200, response1.getStatusCodeValue());
        User user = response1.getBody();
        assertEquals("test", user.getUsername());
        // mock user repository to obtain data
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        final ResponseEntity<User> response2 = userController.findByUserName("test2");
        // we got response
        assertNotNull(response2);
        // status success
        assertEquals(404, response2.getStatusCodeValue());
    }

    @Test
    public void get_user_by_id() throws Exception{
        when(encoder.encode("testpassword")).thenReturn("thisishashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testpassword");
        request.setConfirmPassword("testpassword");
        final ResponseEntity<User> response1 = userController.createUser(request);
        assertEquals(200, response1.getStatusCodeValue());
        User user = response1.getBody();
        assertEquals(0, user.getId());
        // mock user repository to obtain data
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        final ResponseEntity<User> response2 = userController.findById(Long.valueOf(0));
        // we got response
        assertNotNull(response2);
        // status success
        assertEquals(200, response2.getStatusCodeValue());
        User user2 = response2.getBody();
        assertNotNull(user2);
        assertEquals(0, user2.getId());
        assertEquals("test", user2.getUsername());
        assertEquals("thisishashed", user2.getPassword());
    }

    @Test
    public void get_user_by_id_not_found() throws Exception{
        when(encoder.encode("testpassword")).thenReturn("thisishashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testpassword");
        request.setConfirmPassword("testpassword");
        final ResponseEntity<User> response1 = userController.createUser(request);
        assertEquals(200, response1.getStatusCodeValue());
        User user = response1.getBody();
        assertEquals(0, user.getId());
        // mock user repository to obtain data
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        final ResponseEntity<User> response2 = userController.findById(Long.valueOf(10));
        // we got response
        assertNotNull(response2);
        // status success
        assertEquals(404, response2.getStatusCodeValue());
    }
}
