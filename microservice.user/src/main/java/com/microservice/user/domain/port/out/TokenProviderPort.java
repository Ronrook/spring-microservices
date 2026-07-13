package com.microservice.user.domain.port.out;

public interface TokenProviderPort {

    String generateToken(String nickname);

    boolean validateToken(String token);

    String getNicknameFromToken(String token);
}
