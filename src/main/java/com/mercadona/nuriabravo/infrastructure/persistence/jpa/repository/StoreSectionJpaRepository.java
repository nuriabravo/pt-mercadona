package com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository;

import com.mercadona.nuriabravo.infrastructure.persistence.jpa.StoreSectionJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreSectionJpaRepository extends JpaRepository<StoreSectionJpa, Long> {
    Optional<StoreSectionJpa> findByStoreIdAndSectionId(Long storeId, Long sectionId);
}
