package com.eimp.security.service;

import com.eimp.entity.UsersEntity;
import com.eimp.security.entity.RefreshToken;
import com.eimp.security.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public String createRefreshToken(UsersEntity user) {

        String token =
                UUID.randomUUID().toString();

        RefreshToken refreshToken =
                new RefreshToken();

        refreshToken.setToken(token);

        refreshToken.setUser(user);

        refreshToken.setExpiryDate(
                LocalDateTime.now().plusDays(7)
        );

        repository.save(refreshToken);

        return token;
    }
}

