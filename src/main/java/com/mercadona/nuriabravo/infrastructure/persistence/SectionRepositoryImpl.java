package com.mercadona.nuriabravo.infrastructure.persistence;

import com.mercadona.nuriabravo.domain.model.Section;
import com.mercadona.nuriabravo.domain.repository.SectionRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository.SectionJpaRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.mapper.SectionJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SectionRepositoryImpl implements SectionRepository {

    private final SectionJpaRepository jpaRepository;
    private final SectionJpaMapper mapper;

    @Override
    public Optional<Section> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
}