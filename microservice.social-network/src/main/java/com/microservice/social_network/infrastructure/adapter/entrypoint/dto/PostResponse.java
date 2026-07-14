package com.microservice.social_network.infrastructure.adapter.entrypoint.dto;

import java.time.LocalDateTime;

public record PostResponse(Long id, String message, Long userId, LocalDateTime createdAt, int likes) {
}
