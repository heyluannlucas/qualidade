package com.example.demo.service;

import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Testes de Cadastro

    @Test
    public void testRegisterSuccess() {
        User user = new User("John Doe", "john.doe@example.com", "password1");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.register(user);

        assertNotNull(registeredUser);
        assertEquals("John Doe", registeredUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterDuplicateEmail() {
        User user = new User("John Doe", "john.doe@example.com", "password1");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.register(user);
        });

        assertEquals("Email já está em uso.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterShortPassword() {
        User user = new User("John Doe", "john.doe@example.com", "short");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        });

        assertEquals("A senha deve ter pelo menos 8 caracteres.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterPasswordWithoutNumber() {
        User user = new User("John Doe", "john.doe@example.com", "password");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        });

        assertEquals("A senha deve conter pelo menos um número.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterPasswordWithoutLetter() {
        User user = new User("John Doe", "john.doe@example.com", "12345678");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        });

        assertEquals("A senha deve conter pelo menos uma letra.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    // Testes de Login

    @Test
    public void testLoginSuccess() {
        String email = "john.doe@example.com";
        String password = "password1";
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User("John Doe", email, encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User loggedInUser = userService.login(email, password);

        assertNotNull(loggedInUser);
        assertEquals(email, loggedInUser.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testLoginEmailNotFound() {
        String email = "unknown@example.com";
        String password = "password1";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            userService.login(email, password);
        });

        assertEquals("Email ou senha incorreto.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testLoginInvalidPassword() {
        String email = "john.doe@example.com";
        String password = "wrongpassword";
        String encodedPassword = passwordEncoder.encode("password1");

        User user = new User("John Doe", email, encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            userService.login(email, password);
        });

        assertEquals("Email ou senha incorreto.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }
}
