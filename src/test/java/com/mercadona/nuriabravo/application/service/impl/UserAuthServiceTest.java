package com.mercadona.nuriabravo.application.service.impl;

import com.mercadona.nuriabravo.application.dto.output.LoginResponse;
import com.mercadona.nuriabravo.domain.exception.InvalidCredentialsException;
import com.mercadona.nuriabravo.domain.model.User;
import com.mercadona.nuriabravo.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private UserAuthServiceImpl service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "tokenExpirationHours", 24L);
    }

    @Test
    void login_shouldReturnTokenAndExpiration_whenCredentialsAreValid() {
        User user = User.builder().id(1L).username("admin").password("password").build();

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("admin123", "password")).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);

        LoginResponse result = service.login("admin", "admin123");

        assertThat(result.getToken()).isNotBlank();
        assertThat(result.getExpiration()).isAfter(Instant.now());
        verify(userRepository).save(user);
    }

    @Test
    void login_shouldPersistTokenOnUser_whenCredentialsAreValid() {
        User user = User.builder().id(1L).username("admin").password("password").build();

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("admin123", "password")).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);

        service.login("admin", "admin123");

        assertThat(user.getToken()).isNotBlank();
        assertThat(user.getTokenExpiration()).isAfter(Instant.now());
    }

    @Test
    void login_shouldThrowInvalidCredentialsException_whenUsernameDoesNotExist() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> service.login("unknown", "anyPassword"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldThrowInvalidCredentialsException_whenPasswordDoesNotMatch() {
        User user = User.builder().id(1L).username("admin").password("password").build();

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "password")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> service.login("admin", "wrongPassword"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void isTokenValid_shouldReturnTrue_whenTokenExistsAndIsNotExpired() {
        User user = User.builder()
                .token("validToken")
                .tokenExpiration(Instant.now().plusSeconds(3600))
                .build();

        when(userRepository.findByToken("validToken")).thenReturn(Optional.of(user));

        assertThat(service.isTokenValid("validToken")).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenTokenIsExpired() {
        User user = User.builder()
                .token("expiredToken")
                .tokenExpiration(Instant.now().minusSeconds(3600))
                .build();

        when(userRepository.findByToken("expiredToken")).thenReturn(Optional.of(user));

        assertThat(service.isTokenValid("expiredToken")).isFalse();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenTokenDoesNotExist() {
        when(userRepository.findByToken("unknownToken")).thenReturn(Optional.empty());

        assertThat(service.isTokenValid("unknownToken")).isFalse();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenTokenIsNull() {
        assertThat(service.isTokenValid(null)).isFalse();
        verifyNoInteractions(userRepository);
    }

    @Test
    void findByToken_shouldReturnUser_whenTokenExists() {
        User user = User.builder().id(1L).username("admin").token("validToken").build();

        when(userRepository.findByToken("validToken")).thenReturn(Optional.of(user));

        User result = service.findByToken("validToken");

        assertThat(result).isEqualTo(user);
    }

    @Test
    void findByToken_shouldReturnNull_whenTokenDoesNotExist() {
        when(userRepository.findByToken("unknownToken")).thenReturn(Optional.empty());

        User result = service.findByToken("unknownToken");

        assertThat(result).isNull();
    }
}