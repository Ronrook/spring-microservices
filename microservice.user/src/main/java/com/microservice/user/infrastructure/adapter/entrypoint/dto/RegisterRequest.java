package com.microservice.user.infrastructure.adapter.entrypoint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterRequest(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Lastname is required") String lastname,
        @Past(message = "Birthdate must be in the past") LocalDate birthdate,
        @NotBlank(message = "Nickname is required")
        @Size(min = 3, max = 30, message = "Nickname must be between 3 and 30 characters") String nickname,
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters") String password
) {
}
