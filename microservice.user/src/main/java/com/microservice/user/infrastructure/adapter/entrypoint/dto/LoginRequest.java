package com.microservice.user.infrastructure.adapter.entrypoint.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Nickname is required") String nickname,
        @NotBlank(message = "Password is required") String password
) {
}
