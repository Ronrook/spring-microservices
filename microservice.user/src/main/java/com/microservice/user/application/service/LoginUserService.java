package com.microservice.user.application.service;

import com.microservice.user.domain.exception.InvalidCredentialsException;
import com.microservice.user.domain.exception.UserNotFoundException;
import com.microservice.user.domain.model.User;
import com.microservice.user.domain.port.in.LoginUserUseCase;
import com.microservice.user.domain.port.out.PasswordEncoderPort;
import com.microservice.user.domain.port.out.TokenProviderPort;
import com.microservice.user.domain.port.out.UserRepositoryPort;
import com.microservice.user.infrastructure.config.AuditLogger;
import io.micrometer.core.instrument.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginUserService implements LoginUserUseCase {

    private static final Logger log = LoggerFactory.getLogger(LoginUserService.class);

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenProviderPort tokenProvider;
    private final AuditLogger auditLogger;
    private final Counter loginsCounter;
    private final Counter loginsFailedCounter;

    public LoginUserService(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            TokenProviderPort tokenProvider,
            AuditLogger auditLogger,
            Counter userLoginsCounter,
            Counter userLoginsFailedCounter) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.auditLogger = auditLogger;
        this.loginsCounter = userLoginsCounter;
        this.loginsFailedCounter = userLoginsFailedCounter;
    }

    @Override
    @Transactional(readOnly = true)
    public String login(String nickname, String password) {

        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> {
                    auditLogger.logFailure("LOGIN", nickname, "USER_NOT_FOUND");
                    loginsFailedCounter.increment();
                    return new UserNotFoundException(nickname);
                });

        if (!passwordEncoder.matches(password, user.getPassword())) {
            auditLogger.logFailure("LOGIN", nickname, "INVALID_PASSWORD");
            loginsFailedCounter.increment();
            throw new InvalidCredentialsException();
        }

        String token = tokenProvider.generateToken(user.getNickname());
        auditLogger.logSuccess("LOGIN", nickname);
        loginsCounter.increment();
        log.debug("User logged in successfully: {}", nickname);

        return token;
    }
}
