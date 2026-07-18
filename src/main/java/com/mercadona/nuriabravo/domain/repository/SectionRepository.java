package com.mercadona.nuriabravo.domain.repository;

import com.mercadona.nuriabravo.domain.model.Section;

import java.util.Optional;

public interface SectionRepository {
    Optional<Section> findById(Long id);
}