package com.example.demo.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginRequestTest {

    @Test
    public void testLoginRequest() {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password1");

        assertEquals("john.doe@example.com", loginRequest.getEmail());
        assertEquals("password1", loginRequest.getPassword());

        loginRequest.setEmail("jane.doe@example.com");
        loginRequest.setPassword("newPassword");

        assertEquals("jane.doe@example.com", loginRequest.getEmail());
        assertEquals("newPassword", loginRequest.getPassword());
    }
}
