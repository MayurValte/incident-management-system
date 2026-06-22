package com.eimp.security.dto;

public record LoginRequest(
        String username,
        String password
) {
}