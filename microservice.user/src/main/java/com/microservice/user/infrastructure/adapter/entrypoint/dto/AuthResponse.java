package com.microservice.user.infrastructure.adapter.entrypoint.dto;

public record AuthResponse(String token, String nickname) {
}
