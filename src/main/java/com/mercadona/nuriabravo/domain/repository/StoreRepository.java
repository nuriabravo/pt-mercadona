package com.mercadona.nuriabravo.domain.repository;

import com.mercadona.nuriabravo.domain.model.Store;

import java.util.Optional;

public interface StoreRepository {
    Optional<Store> findById(Long id);
}