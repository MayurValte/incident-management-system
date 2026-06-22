package com.eimp.security.authController;

import com.eimp.entity.UsersEntity;
import com.eimp.security.dto.ChangePasswordRequest;
import com.eimp.security.dto.LoginRequest;
import com.eimp.security.dto.LoginResponse;
import com.eimp.security.dto.RefreshRequest;
import com.eimp.security.entity.RefreshToken;
import com.eimp.security.repository.RefreshTokenRepository;
import com.eimp.security.service.JwtService;
import com.eimp.security.service.RefreshTokenService;
import com.eimp.security.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        UsersEntity user =
                (UsersEntity) authentication.getPrincipal();

        String accessToken =
                jwtService.generateToken(
                        request.username());

        String refreshToken =
                refreshTokenService
                        .createRefreshToken(user);

        return ResponseEntity.ok(new LoginResponse(
                accessToken,
                refreshToken,
                user.getPasswordChangeRequired())
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @RequestBody RefreshRequest request) {

        RefreshToken token = refreshTokenRepository
                .findByToken(request.refreshToken())
                .orElseThrow();

        if (token.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "Refresh Token Expired");
        }

        String accessToken =
                jwtService.generateToken(
                        token.getUser()
                                .getEmail()
                );

        return ResponseEntity.ok(new LoginResponse(
                accessToken,
                token.getToken(),
                false
        ));
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal principal) {

        return ResponseEntity.ok(userService.changePassword(request, principal));

    }
}
