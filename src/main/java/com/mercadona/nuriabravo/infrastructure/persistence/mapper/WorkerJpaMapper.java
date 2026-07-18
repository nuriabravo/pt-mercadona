package com.mercadona.nuriabravo.infrastructure.persistence.mapper;

import com.mercadona.nuriabravo.domain.model.Worker;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.WorkerJpa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = StoreJpaMapper.class)
public interface WorkerJpaMapper {
    Worker toDomain(WorkerJpa entity);
    WorkerJpa toEntity(Worker domain);
}
