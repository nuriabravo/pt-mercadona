package com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository;

import com.mercadona.nuriabravo.infrastructure.persistence.jpa.StoreJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreJpaRepository extends JpaRepository<StoreJpa, Long> {
    Optional<StoreJpa> findByCode(String code);
}
