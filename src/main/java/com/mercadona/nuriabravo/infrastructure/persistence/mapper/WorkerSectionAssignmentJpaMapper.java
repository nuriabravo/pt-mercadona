package com.mercadona.nuriabravo.infrastructure.persistence.mapper;

import com.mercadona.nuriabravo.domain.model.WorkerSectionAssignment;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.WorkerSectionAssignmentJpa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {WorkerJpaMapper.class, StoreSectionJpaMapper.class})
public interface WorkerSectionAssignmentJpaMapper {
    WorkerSectionAssignment toDomain(WorkerSectionAssignmentJpa entity);
    WorkerSectionAssignmentJpa toEntity(WorkerSectionAssignment domain);
}