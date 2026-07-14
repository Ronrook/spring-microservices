package com.microservice.user.infrastructure.adapter.entrypoint.dto;

import java.time.LocalDate;

public record UserResponse(Long id, String name, String lastname, LocalDate birthdate, String nickname) {
}
