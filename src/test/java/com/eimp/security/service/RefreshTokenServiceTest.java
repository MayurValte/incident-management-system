package com.eimp.security.service;

import com.eimp.entity.UsersEntity;
import com.eimp.security.entity.RefreshToken;
import com.eimp.security.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository repository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void shouldCreateRefreshTokenSuccessfully() {

        UsersEntity user = new UsersEntity();
        user.setId(1L);
        user.setEmail("mayur@test.com");

        String token =
                refreshTokenService.createRefreshToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());

        ArgumentCaptor<RefreshToken> captor =
                ArgumentCaptor.forClass(
                        RefreshToken.class);

        verify(repository)
                .save(captor.capture());

        RefreshToken savedToken =
                captor.getValue();

        assertEquals(
                user,
                savedToken.getUser());

        assertEquals(
                token,
                savedToken.getToken());

        assertNotNull(
                savedToken.getExpiryDate());
    }
    @Test
    void shouldSetExpiryDateSevenDaysAhead() {

        UsersEntity user = new UsersEntity();

        LocalDateTime before =
                LocalDateTime.now();

        refreshTokenService.createRefreshToken(user);

        ArgumentCaptor<RefreshToken> captor =
                ArgumentCaptor.forClass(
                        RefreshToken.class);

        verify(repository)
                .save(captor.capture());

        LocalDateTime expiry =
                captor.getValue()
                        .getExpiryDate();

        LocalDateTime expected =
                before.plusDays(7);

        assertEquals(
                expected.getDayOfMonth(),
                expiry.getDayOfMonth());
    }
}