package com.mercadona.nuriabravo.infrastructure.persistence.mapper;

import com.mercadona.nuriabravo.domain.model.Section;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.SectionJpa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SectionJpaMapper {
    Section toDomain(SectionJpa entity);
    SectionJpa toEntity(Section domain);
}