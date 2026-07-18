package com.mercadona.nuriabravo.infrastructure.persistence;

import com.mercadona.nuriabravo.domain.model.WorkerSectionAssignment;
import com.mercadona.nuriabravo.domain.repository.WorkerSectionAssignmentRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.WorkerSectionAssignmentJpa;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository.WorkerSectionAssignmentJpaRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.mapper.WorkerSectionAssignmentJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WorkerSectionAssignmentRepositoryImpl implements WorkerSectionAssignmentRepository {

    private final WorkerSectionAssignmentJpaRepository jpaRepository;
    private final WorkerSectionAssignmentJpaMapper mapper;

    @Override
    public List<WorkerSectionAssignment> findByWorkerId(Long workerId) {
        return jpaRepository.findByWorkerId(workerId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<WorkerSectionAssignment> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public WorkerSectionAssignment save(WorkerSectionAssignment assignment) {
        WorkerSectionAssignmentJpa entity = mapper.toEntity(assignment);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<WorkerSectionAssignment> findByStoreSectionId(Long storeSectionId) {
        return jpaRepository.findByStoreSectionId(storeSectionId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}