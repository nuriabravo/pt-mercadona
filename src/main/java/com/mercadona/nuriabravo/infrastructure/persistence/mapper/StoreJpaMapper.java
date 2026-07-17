package com.mercadona.nuriabravo.infrastructure.persistence.mapper;

import com.mercadona.nuriabravo.domain.model.Store;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.StoreJpa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreJpaMapper {
    Store toDomain(StoreJpa entity);
}
