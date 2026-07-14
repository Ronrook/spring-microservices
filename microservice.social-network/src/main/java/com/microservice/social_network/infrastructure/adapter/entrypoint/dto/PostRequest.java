package com.microservice.social_network.infrastructure.adapter.entrypoint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotBlank(message = "Message is required")
        @Size(max = 500, message = "Message must not exceed 500 characters") String message,
        @NotBlank(message = "User ID is required") String userId
) {
}
