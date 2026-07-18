package com.mercadona.nuriabravo.domain.repository;

import com.mercadona.nuriabravo.domain.model.WorkerSectionAssignment;

import java.util.List;
import java.util.Optional;

public interface WorkerSectionAssignmentRepository {
    List<WorkerSectionAssignment> findByWorkerId(Long workerId);
    Optional<WorkerSectionAssignment> findById(Long id);
    WorkerSectionAssignment save(WorkerSectionAssignment assignment);
    void deleteById(Long id);
}