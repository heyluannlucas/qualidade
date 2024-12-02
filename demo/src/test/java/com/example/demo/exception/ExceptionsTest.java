package com.example.demo.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionsTest {

    @Test
    public void testEmailAlreadyExistsException() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException("Email já está em uso.");
        assertEquals("Email já está em uso.", exception.getMessage());
    }

    @Test
    public void testInvalidCredentialsException() {
        InvalidCredentialsException exception = new InvalidCredentialsException("Invalid credentials");
        assertEquals("Invalid credentials", exception.getMessage());
    }
}
