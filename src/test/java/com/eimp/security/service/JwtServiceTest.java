package com.eimp.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void shouldGenerateToken() {

        String token =
                jwtService.generateToken("mayur@test.com");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractUsernameFromToken() {

        String username = "mayur@test.com";

        String token =
                jwtService.generateToken(username);

        String extractedUsername =
                jwtService.extractUsername(token);

        assertEquals(
                username,
                extractedUsername
        );
    }

    @Test
    void shouldValidateGeneratedToken() {

        String token =
                jwtService.generateToken("mayur@test.com");

        boolean valid =
                jwtService.isValid(token);

        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseForInvalidToken() {

        String invalidToken =
                "this.is.invalid.token";

        boolean valid =
                jwtService.isValid(invalidToken);

        assertFalse(valid);
    }

    @Test
    void shouldGenerateDifferentTokensForDifferentUsers() {

        String token1 =
                jwtService.generateToken("mayur@test.com");

        String token2 =
                jwtService.generateToken("admin@test.com");

        assertNotEquals(token1, token2);
    }

    @Test
    void shouldThrowExceptionForMalformedTokenWhileExtractingUsername() {

        String invalidToken =
                "invalid.jwt.token";

        assertThrows(
                Exception.class,
                () -> jwtService.extractUsername(invalidToken)
        );
    }
}