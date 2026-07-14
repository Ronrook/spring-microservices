package com.microservice.user.infrastructure.adapter.entrypoint.mapper;

import com.microservice.user.domain.model.User;
import com.microservice.user.infrastructure.adapter.entrypoint.dto.RegisterRequest;
import com.microservice.user.infrastructure.adapter.entrypoint.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(RegisterRequest request) {
        return new User(
                request.name(),
                request.lastname(),
                request.birthdate(),
                request.nickname(),
                request.password()
        );
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getBirthdate(),
                user.getNickname()
        );
    }
}
