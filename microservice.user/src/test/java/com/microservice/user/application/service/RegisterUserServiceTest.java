package com.microservice.user.application.service;

import com.microservice.user.domain.exception.UserAlreadyExistsException;
import com.microservice.user.domain.model.Role;
import com.microservice.user.domain.model.User;
import com.microservice.user.domain.port.out.PasswordEncoderPort;
import com.microservice.user.domain.port.out.RoleRepositoryPort;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private RoleRepositoryPort roleRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private AuditLogger auditLogger;

    @Mock
    private Counter userRegistrationsCounter;

    private RegisterUserService registerUserService;

    private Role userRole;
    private User testUser;

    @BeforeEach
    void setUp() {
        registerUserService = new RegisterUserService(
                userRepository, roleRepository, passwordEncoder,
                auditLogger, userRegistrationsCounter);
        userRole = new Role(1L, "USER");
        testUser = new User("Carlos", "Garcia",
                LocalDate.of(1990, 3, 15), "carlos", "password123");
    }

    @Test
    void register_withValidData_returnsSavedUser() {
        when(userRepository.existsByNickname("carlos")).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");

        User savedUser = new User("Carlos", "Garcia",
                LocalDate.of(1990, 3, 15), "carlos", "encodedPass");
        savedUser.addRole(userRole);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = registerUserService.register(testUser);

        assertNotNull(result);
        verify(userRepository).existsByNickname("carlos");
        verify(roleRepository).findByName("USER");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(auditLogger).logSuccess("REGISTER", "carlos");
        verify(userRegistrationsCounter).increment();
    }

    @Test
    void register_withExistingNickname_throwsUserAlreadyExistsException() {
        when(userRepository.existsByNickname("carlos")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> registerUserService.register(testUser));

        verify(auditLogger).logFailure("REGISTER", "carlos", "USER_ALREADY_EXISTS");
        verify(userRegistrationsCounter, never()).increment();
    }

    @Test
    void register_withoutDefaultRole_throwsIllegalStateException() {
        when(userRepository.existsByNickname("carlos")).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> registerUserService.register(testUser));

        verify(auditLogger).logFailure("REGISTER", "carlos", "DEFAULT_ROLE_NOT_FOUND");
        verify(userRegistrationsCounter, never()).increment();
    }
}
