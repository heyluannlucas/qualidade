package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @Test
    public void testRegisterSuccess() {
        User user = new User("John Doe", "john.doe@example.com", "password1");

        when(userService.register(Mockito.any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userController.register(user);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testRegisterError() {
        User user = new User("John Doe", "john.doe@example.com", "password1");

        when(userService.register(Mockito.any(User.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<?> response = userController.register(user);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid data", response.getBody());
    }

    @Test
    public void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password1");
        User user = new User("John Doe", loginRequest.getEmail(), "encodedPassword");

        when(userService.login(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(user);

        ResponseEntity<?> response = userController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testLoginError() {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "wrongPassword");

        when(userService.login(loginRequest.getEmail(), loginRequest.getPassword()))
                .thenThrow(new IllegalArgumentException("Invalid credentials"));

        ResponseEntity<?> response = userController.login(loginRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid credentials", response.getBody());
    }
}
