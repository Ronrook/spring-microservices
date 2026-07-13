package com.microservice.user.application.service;

import com.microservice.user.domain.exception.UserAlreadyExistsException;
import com.microservice.user.domain.model.Role;
import com.microservice.user.domain.model.User;
import com.microservice.user.domain.port.in.RegisterUserUseCase;
import com.microservice.user.domain.port.out.PasswordEncoderPort;
import com.microservice.user.domain.port.out.RoleRepositoryPort;
import com.microservice.user.domain.port.out.UserRepositoryPort;
import com.microservice.user.infrastructure.config.AuditLogger;
import io.micrometer.core.instrument.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterUserService implements RegisterUserUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegisterUserService.class);

    private final UserRepositoryPort userRepository;
    private final RoleRepositoryPort roleRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final AuditLogger auditLogger;
    private final Counter registrationsCounter;

    public RegisterUserService(
            UserRepositoryPort userRepository,
            RoleRepositoryPort roleRepository,
            PasswordEncoderPort passwordEncoder,
            AuditLogger auditLogger,
            Counter userRegistrationsCounter) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditLogger = auditLogger;
        this.registrationsCounter = userRegistrationsCounter;
    }

    @Override
    @Transactional
    public User register(User user) {

        if (userRepository.existsByNickname(user.getNickname())) {
            auditLogger.logFailure("REGISTER", user.getNickname(), "USER_ALREADY_EXISTS");
            throw new UserAlreadyExistsException();
        }

        Role defaultRole = roleRepository
                .findByName("USER")
                .orElseThrow(() -> {
                    auditLogger.logFailure("REGISTER", user.getNickname(), "DEFAULT_ROLE_NOT_FOUND");
                    return new IllegalStateException("Default role USER not found");
                });

        user.addRole(defaultRole);

        user.updatePassword(
                passwordEncoder.encode(user.getPassword())
        );

        User saved = userRepository.save(user);
        auditLogger.logSuccess("REGISTER", user.getNickname());
        registrationsCounter.increment();
        log.debug("User registered successfully: {}", user.getNickname());

        return saved;
    }
}
