package com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import com.mercadona.nuriabravo.infrastructure.persistence.jpa.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserJpa, Long> {
    Optional<UserJpa> findByUsername(String username);
    Optional<UserJpa> findByToken(String token);
}
