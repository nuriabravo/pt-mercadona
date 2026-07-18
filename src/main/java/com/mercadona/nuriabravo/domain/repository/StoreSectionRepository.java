package com.mercadona.nuriabravo.domain.repository;

import com.mercadona.nuriabravo.domain.model.StoreSection;

import java.util.List;
import java.util.Optional;

public interface StoreSectionRepository {
    Optional<StoreSection> findByStoreIdAndSectionId(Long storeId, Long sectionId);
    StoreSection save(StoreSection storeSection);
    List<StoreSection> findByStoreId(Long storeId);
}