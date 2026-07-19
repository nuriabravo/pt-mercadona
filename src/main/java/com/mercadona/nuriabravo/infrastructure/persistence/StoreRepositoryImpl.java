package com.mercadona.nuriabravo.infrastructure.persistence;

import com.mercadona.nuriabravo.domain.model.Store;
import com.mercadona.nuriabravo.domain.repository.StoreRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository.StoreJpaRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.mapper.StoreJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {

    private final StoreJpaRepository jpaRepository;
    private final StoreJpaMapper mapper;

    @Override
    public Optional<Store> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Store> findByCode(String code) {
        return jpaRepository.findByCode(code).map(mapper::toDomain);
    }
}