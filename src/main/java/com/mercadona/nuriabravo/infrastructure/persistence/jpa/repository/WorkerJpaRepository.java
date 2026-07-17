package com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository;

import com.mercadona.nuriabravo.infrastructure.persistence.jpa.WorkerJpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerJpaRepository extends JpaRepository<WorkerJpa, Long> {
}
