package com.microservice.user.application.service;

import com.microservice.user.domain.exception.InvalidCredentialsException;
import com.microservice.user.domain.exception.UserNotFoundException;
import com.microservice.user.domain.model.User;
import com.microservice.user.domain.port.out.PasswordEncoderPort;
import com.microservice.user.domain.port.out.TokenProviderPort;
import com.microservice.user.domain.port.out.UserRepositoryPort;
import com.microservice.user.infrastructure.config.AuditLogger;
import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private TokenProviderPort tokenProvider;

    @Mock
    private AuditLogger auditLogger;

    @Mock
    private Counter userLoginsCounter;

    @Mock
    private Counter userLoginsFailedCounter;

    private LoginUserService loginUserService;

    private User testUser;

    @BeforeEach
    void setUp() {
        loginUserService = new LoginUserService(
                userRepository, passwordEncoder, tokenProvider,
                auditLogger, userLoginsCounter, userLoginsFailedCounter);
        testUser = new User("Carlos", "Garcia",
                LocalDate.of(1990, 3, 15), "carlos", "encodedPass");
    }

    @Test
    void login_withValidCredentials_returnsToken() {
        when(userRepository.findByNickname("carlos")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPass")).thenReturn(true);
        when(tokenProvider.generateToken("carlos")).thenReturn("jwt-token-123");

        String token = loginUserService.login("carlos", "password123");

        assertEquals("jwt-token-123", token);
        verify(userRepository).findByNickname("carlos");
        verify(passwordEncoder).matches("password123", "encodedPass");
        verify(tokenProvider).generateToken("carlos");
        verify(auditLogger).logSuccess("LOGIN", "carlos");
        verify(userLoginsCounter).increment();
        verify(userLoginsFailedCounter, never()).increment();
    }

    @Test
    void login_withNonExistentUser_throwsUserNotFoundException() {
        when(userRepository.findByNickname("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> loginUserService.login("unknown", "password123"));

        verify(auditLogger).logFailure("LOGIN", "unknown", "USER_NOT_FOUND");
        verify(userLoginsFailedCounter).increment();
        verify(userLoginsCounter, never()).increment();
    }

    @Test
    void login_withInvalidPassword_throwsInvalidCredentialsException() {
        when(userRepository.findByNickname("carlos")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", "encodedPass")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> loginUserService.login("carlos", "wrongpassword"));

        verify(auditLogger).logFailure("LOGIN", "carlos", "INVALID_PASSWORD");
        verify(userLoginsFailedCounter).increment();
        verify(userLoginsCounter, never()).increment();
    }
}
