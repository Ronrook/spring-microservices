package com.microservice.user.application.service;

import com.microservice.user.domain.exception.UserNotFoundException;
import com.microservice.user.domain.model.User;
import com.microservice.user.domain.port.in.GetUserProfileUseCase;
import com.microservice.user.domain.port.out.UserRepositoryPort;
import com.microservice.user.infrastructure.config.AuditLogger;
import io.micrometer.core.instrument.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetUserProfileService implements GetUserProfileUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetUserProfileService.class);
    private final UserRepositoryPort userRepository;
    private final AuditLogger auditLogger;
    private final Counter profileViewsCounter;

    public GetUserProfileService(UserRepositoryPort userRepository, AuditLogger auditLogger,
                                 Counter userProfileViewsCounter) {
        this.userRepository = userRepository;
        this.auditLogger = auditLogger;
        this.profileViewsCounter = userProfileViewsCounter;
    }

    @Override
    @Transactional(readOnly = true)
    public User getProfile(String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> {
                    auditLogger.logFailure("VIEW_PROFILE", nickname, "USER_NOT_FOUND");
                    return new UserNotFoundException(nickname);
                });

        auditLogger.logAccess("VIEW_PROFILE", nickname, "profile");
        profileViewsCounter.increment();
        log.debug("Profile viewed for user: {}", nickname);

        return user;
    }

}
