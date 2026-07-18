package com.mercadona.nuriabravo.infrastructure.persistence;

import com.mercadona.nuriabravo.domain.model.StoreSection;
import com.mercadona.nuriabravo.domain.repository.StoreSectionRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.StoreSectionJpa;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository.StoreSectionJpaRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.mapper.StoreSectionJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StoreSectionRepositoryImpl implements StoreSectionRepository {

    private final StoreSectionJpaRepository jpaRepository;
    private final StoreSectionJpaMapper mapper;

    @Override
    public Optional<StoreSection> findByStoreIdAndSectionId(Long storeId, Long sectionId) {
        return jpaRepository.findByStoreIdAndSectionId(storeId, sectionId).map(mapper::toDomain);
    }

    @Override
    public StoreSection save(StoreSection storeSection) {
        StoreSectionJpa entity = mapper.toEntity(storeSection);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public List<StoreSection> findByStoreId(Long storeId) {
        return jpaRepository.findByStoreId(storeId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}