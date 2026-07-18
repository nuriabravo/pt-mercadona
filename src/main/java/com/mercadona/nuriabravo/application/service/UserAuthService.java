package com.mercadona.nuriabravo.application.service;

import com.mercadona.nuriabravo.application.dto.output.LoginResponse;
import com.mercadona.nuriabravo.domain.model.User;

public interface UserAuthService {
    boolean isTokenValid(String token);
    User findByToken(String token);
    LoginResponse login(String username, String rawPassword);
}