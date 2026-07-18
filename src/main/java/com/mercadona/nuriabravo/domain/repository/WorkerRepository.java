package com.mercadona.nuriabravo.domain.repository;

import com.mercadona.nuriabravo.domain.model.Worker;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository {
    List<Worker> findAll();
    Optional<Worker> findById(Long id);
    Worker save(Worker worker);
    void deleteById(Long id);
    boolean existsById(Long id);
}