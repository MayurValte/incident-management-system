package com.eimp.security;

import com.eimp.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    void shouldContinueFilterChainWhenAuthorizationHeaderMissing()
            throws ServletException, IOException {

        when(request.getHeader("Authorization"))
                .thenReturn(null);

        jwtFilter.doFilterInternal(
                request,
                response,
                filterChain);

        verify(filterChain)
                .doFilter(request, response);

        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldContinueFilterChainWhenTokenInvalid()
            throws ServletException, IOException {

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer invalid-token");

        when(jwtService.isValid("invalid-token"))
                .thenReturn(false);

        jwtFilter.doFilterInternal(
                request,
                response,
                filterChain);

        verify(filterChain)
                .doFilter(request, response);

        verify(jwtService)
                .isValid("invalid-token");
    }

    @Test
    void shouldAuthenticateUserForValidToken()
            throws ServletException, IOException {

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername("mayur@test.com")
                        .password("password")
                        .authorities("ROLE_USER")
                        .build();

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer valid-token");

        when(jwtService.isValid("valid-token"))
                .thenReturn(true);

        when(jwtService.extractUsername("valid-token"))
                .thenReturn("mayur@test.com");

        when(userDetailsService
                .loadUserByUsername("mayur@test.com"))
                .thenReturn(userDetails);

        jwtFilter.doFilterInternal(
                request,
                response,
                filterChain);

        assertNotNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication());

        verify(filterChain)
                .doFilter(request, response);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
}