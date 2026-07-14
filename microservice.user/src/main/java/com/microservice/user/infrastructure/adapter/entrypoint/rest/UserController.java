package com.microservice.user.infrastructure.adapter.entrypoint.rest;

import com.microservice.user.domain.port.in.GetUserProfileUseCase;
import com.microservice.user.infrastructure.adapter.entrypoint.dto.UserResponse;
import com.microservice.user.infrastructure.adapter.entrypoint.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User profile management")
public class UserController {

    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UserMapper userMapper;

    public UserController(GetUserProfileUseCase getUserProfileUseCase, UserMapper userMapper) {
        this.getUserProfileUseCase = getUserProfileUseCase;
        this.userMapper = userMapper;
    }

    @GetMapping("/profile")
    @Operation(summary = "Get authenticated user profile", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        String nickname = (String) authentication.getPrincipal();
        var user = getUserProfileUseCase.getProfile(nickname);
        return ResponseEntity.ok(userMapper.toResponse(user));
    }
}
