package com.microservice.user.infrastructure.adapter.driven.security;

import com.microservice.user.domain.port.out.TokenProviderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderAdapterTest {

    private TokenProviderPort tokenProvider;

    private static final String SECRET = "Zm9vLXNlY3JldC1rZXktZm9yLXNvY2lhbC1uZXR3b3JrLWFwcC1qd3QtdG9rZW4tc2lnbmluZw==";
    private static final long EXPIRATION = 3600000;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProviderAdapter(SECRET, EXPIRATION);
    }

    @Test
    void generateToken_returnsValidToken() {
        String token = tokenProvider.generateToken("carlos");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void validateToken_withValidToken_returnsTrue() {
        String token = tokenProvider.generateToken("carlos");

        assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    void validateToken_withInvalidToken_returnsFalse() {
        assertFalse(tokenProvider.validateToken("invalid-token"));
    }

    @Test
    void getNicknameFromToken_withValidToken_returnsNickname() {
        String token = tokenProvider.generateToken("carlos");

        String nickname = tokenProvider.getNicknameFromToken(token);

        assertEquals("carlos", nickname);
    }

    @Test
    void generateToken_withDifferentNicknames_producesDifferentTokens() {
        String token1 = tokenProvider.generateToken("carlos");
        String token2 = tokenProvider.generateToken("maria");

        assertNotEquals(token1, token2);
    }
}
