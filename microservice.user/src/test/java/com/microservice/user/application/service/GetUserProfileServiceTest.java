package com.microservice.user.application.service;

import com.microservice.user.domain.exception.UserNotFoundException;
import com.microservice.user.domain.model.User;
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
class GetUserProfileServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private AuditLogger auditLogger;

    @Mock
    private Counter userProfileViewsCounter;

    private GetUserProfileService getUserProfileService;

    private User testUser;

    @BeforeEach
    void setUp() {
        getUserProfileService = new GetUserProfileService(
                userRepository, auditLogger, userProfileViewsCounter);
        testUser = new User("Carlos", "Garcia",
                LocalDate.of(1990, 3, 15), "carlos", "password123");
    }

    @Test
    void getProfile_withValidNickname_returnsUser() {
        when(userRepository.findByNickname("carlos")).thenReturn(Optional.of(testUser));

        User result = getUserProfileService.getProfile("carlos");

        assertNotNull(result);
        assertEquals("carlos", result.getNickname());
        assertEquals("Carlos", result.getName());
        verify(auditLogger).logAccess("VIEW_PROFILE", "carlos", "profile");
        verify(userProfileViewsCounter).increment();
    }

    @Test
    void getProfile_withNonExistentNickname_throwsUserNotFoundException() {
        when(userRepository.findByNickname("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> getUserProfileService.getProfile("unknown"));

        verify(auditLogger).logFailure("VIEW_PROFILE", "unknown", "USER_NOT_FOUND");
        verify(userProfileViewsCounter, never()).increment();
    }
}
