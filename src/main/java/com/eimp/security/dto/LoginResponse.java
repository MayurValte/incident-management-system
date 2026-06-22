package com.eimp.security.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        Boolean passwordChangeRequired
) {
}