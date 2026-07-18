package com.mercadona.nuriabravo.infrastructure.persistence.mapper;

import com.mercadona.nuriabravo.domain.model.StoreSection;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.StoreSectionJpa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StoreJpaMapper.class, SectionJpaMapper.class})
public interface StoreSectionJpaMapper {
    StoreSection toDomain(StoreSectionJpa entity);
    StoreSectionJpa toEntity(StoreSection domain);
}