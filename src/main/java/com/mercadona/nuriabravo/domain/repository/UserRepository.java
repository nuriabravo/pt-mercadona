package com.mercadona.nuriabravo.domain.repository;

import com.mercadona.nuriabravo.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByToken(String token);
    Optional<User> findByUsername(String username);
    User save(User jpa);
}
