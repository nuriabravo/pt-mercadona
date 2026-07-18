package com.mercadona.nuriabravo.application.service.impl;

import com.mercadona.nuriabravo.application.dto.output.LoginResponse;
import com.mercadona.nuriabravo.application.service.UserAuthService;
import com.mercadona.nuriabravo.domain.exception.InvalidCredentialsException;
import com.mercadona.nuriabravo.domain.model.User;
import com.mercadona.nuriabravo.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.token.expiration}")
    private long tokenExpirationHours;

    @Transactional
    @Override
    public LoginResponse login(String username, String rawPassword) {
        User user = getUserOrThrow(username);
        validateCredentials(rawPassword, user);

        return generateAndPersistToken(user);
    }

    private LoginResponse generateAndPersistToken(User user) {
        String token = UUID.randomUUID().toString();
        Instant expiration = Instant.now().plus(tokenExpirationHours, ChronoUnit.HOURS);

        user.setToken(token);
        user.setTokenExpiration(expiration);
        userRepository.save(user);

        return new LoginResponse(token, expiration);
    }

    @Override
    public boolean isTokenValid(String token) {
        return token != null && userRepository.findByToken(token)
                .filter(this::isNotExpired)
                .isPresent();
    }

    @Override
    public User findByToken(String token) {
        return userRepository.findByToken(token).orElse(null);
    }

    private User getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);
    }

    private void validateCredentials(String rawPassword, User user) {
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException();
        }
    }

    private boolean isNotExpired(User user) {
        return user.getTokenExpiration() != null && user.getTokenExpiration().isAfter(Instant.now());
    }
}