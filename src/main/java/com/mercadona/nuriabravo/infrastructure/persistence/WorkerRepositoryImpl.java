package com.mercadona.nuriabravo.infrastructure.persistence;

import com.mercadona.nuriabravo.domain.model.Worker;
import com.mercadona.nuriabravo.domain.repository.WorkerRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.WorkerJpa;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository.WorkerJpaRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.mapper.WorkerJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WorkerRepositoryImpl implements WorkerRepository {

    private final WorkerJpaRepository jpaRepository;
    private final WorkerJpaMapper mapper;

    @Override
    public List<Worker> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Worker> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Worker save(Worker worker) {
        WorkerJpa entity = mapper.toEntity(worker);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}