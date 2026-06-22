package com.eimp.security.service;

import com.eimp.entity.UsersEntity;
import com.eimp.exception.BadRequestException;
import com.eimp.repository.UserRepository;
import com.eimp.security.dto.ChangePasswordRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Principal principal;

    @InjectMocks
    private UserService userService;

    private UsersEntity user;
    private ChangePasswordRequest request;

    @BeforeEach
    void setUp() {

        user = new UsersEntity();
        user.setId(1L);
        user.setEmail("mayur@test.com");
        user.setPassword("encodedOldPassword");

        request = new ChangePasswordRequest(
                "Old@123",
                "New@123",
                "New@123"
        );
    }

    @Test
    void shouldChangePasswordSuccessfully() {

        when(principal.getName())
                .thenReturn("mayur@test.com");

        when(userRepository.findByEmail("mayur@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "Old@123",
                "encodedOldPassword"))
                .thenReturn(true);

        when(passwordEncoder.encode("New@123"))
                .thenReturn("encodedNewPassword");

        String result =
                userService.changePassword(
                        request,
                        principal);

        assertEquals("Password changed successfully",
                result);

        assertEquals(
                "encodedNewPassword",
                user.getPassword());

        assertFalse(
                user.getPasswordChangeRequired());

        verify(userRepository)
                .save(user);
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordIncorrect() {

        when(principal.getName())
                .thenReturn("mayur@test.com");

        when(userRepository.findByEmail("mayur@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "Old@123",
                "encodedOldPassword"))
                .thenReturn(false);

        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> userService.changePassword(
                                request,
                                principal)
                );

        assertEquals(
                "Current Password is incorrect",
                exception.getMessage());

        verify(userRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenPasswordsDoNotMatch() {

        ChangePasswordRequest request =
                new ChangePasswordRequest(
                        "Old@123",
                        "New@123",
                        "Different@123"
                );

        when(principal.getName())
                .thenReturn("mayur@test.com");

        when(userRepository.findByEmail("mayur@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "Old@123",
                "encodedOldPassword"))
                .thenReturn(true);

        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> userService.changePassword(
                                request,
                                principal)
                );

        assertEquals(
                "New Password and Confirmed Password do not match",
                exception.getMessage());

        verify(userRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenNewPasswordSameAsCurrentPassword() {

        when(principal.getName())
                .thenReturn("mayur@test.com");

        when(userRepository.findByEmail("mayur@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "Old@123",
                "encodedOldPassword"))
                .thenReturn(true);

        when(passwordEncoder.matches(
                "New@123",
                "encodedOldPassword"))
                .thenReturn(true);

        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> userService.changePassword(
                                request,
                                principal)
                );

        assertEquals(
                "New Password cannot be same as current password",
                exception.getMessage());

        verify(userRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(principal.getName())
                .thenReturn("mayur@test.com");

        when(userRepository.findByEmail("mayur@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> userService.changePassword(
                        request,
                        principal)
        );
    }
}