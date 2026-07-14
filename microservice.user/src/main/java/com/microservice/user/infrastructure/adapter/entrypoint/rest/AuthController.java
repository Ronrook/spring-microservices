package com.microservice.user.infrastructure.adapter.entrypoint.rest;

import com.microservice.user.domain.port.in.LoginUserUseCase;
import com.microservice.user.domain.port.in.RegisterUserUseCase;
import com.microservice.user.infrastructure.adapter.entrypoint.dto.AuthResponse;
import com.microservice.user.infrastructure.adapter.entrypoint.dto.LoginRequest;
import com.microservice.user.infrastructure.adapter.entrypoint.dto.RegisterRequest;
import com.microservice.user.infrastructure.adapter.entrypoint.dto.UserResponse;
import com.microservice.user.infrastructure.adapter.entrypoint.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication and registration")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final UserMapper userMapper;


    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUserUseCase loginUserUseCase,
                          UserMapper userMapper) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        var user = userMapper.toDomain(request);
        var registered = registerUserUseCase.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(registered));
    }

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = loginUserUseCase.login(request.nickname(), request.password());
        return ResponseEntity.ok(new AuthResponse(token, request.nickname()));
    }
}
