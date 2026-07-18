package com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository;

import com.mercadona.nuriabravo.infrastructure.persistence.jpa.WorkerSectionAssignmentJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkerSectionAssignmentJpaRepository extends JpaRepository<WorkerSectionAssignmentJpa, Long> {
    List<WorkerSectionAssignmentJpa> findByWorkerId(Long workerId);
}
