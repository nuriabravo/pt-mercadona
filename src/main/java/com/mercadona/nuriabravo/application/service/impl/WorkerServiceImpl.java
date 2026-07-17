package com.mercadona.nuriabravo.application.service.impl;

import com.mercadona.nuriabravo.application.dto.input.WorkerRequestDto;
import com.mercadona.nuriabravo.application.dto.output.WorkerResponseDto;
import com.mercadona.nuriabravo.application.service.WorkerService;
import com.mercadona.nuriabravo.domain.exception.StoreNotFoundException;
import com.mercadona.nuriabravo.domain.exception.WorkerNotFoundException;
import com.mercadona.nuriabravo.domain.mapper.WorkerMapper;
import com.mercadona.nuriabravo.domain.model.Store;
import com.mercadona.nuriabravo.domain.model.Worker;
import com.mercadona.nuriabravo.domain.repository.StoreRepository;
import com.mercadona.nuriabravo.domain.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository;
    private final StoreRepository storeRepository;
    private final WorkerMapper workerMapper;

    @Override
    @Transactional(readOnly = true)
    public List<WorkerResponseDto> findAll() {
        return workerRepository.findAll().stream()
                .map(workerMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WorkerResponseDto findById(Long id) {
        return workerMapper.toResponseDto(getWorkerOrThrow(id));
    }

    @Override
    public WorkerResponseDto create(WorkerRequestDto dto) {
        Store store = getStoreOrThrow(dto.getStoreId());

        Worker worker = workerMapper.toDomain(dto);
        worker.setStore(store);

        return workerMapper.toResponseDto(workerRepository.save(worker));
    }

    @Override
    public WorkerResponseDto update(Long id, WorkerRequestDto dto) {
        Worker worker = getWorkerOrThrow(id);
        Store store = getStoreOrThrow(dto.getStoreId());

        workerMapper.updateDomainFromDto(dto, worker);
        worker.setStore(store);

        return workerMapper.toResponseDto(workerRepository.save(worker));
    }

    @Override
    public void delete(Long id) {
        if (!workerRepository.existsById(id)) {
            throw new WorkerNotFoundException(id);
        }
        workerRepository.deleteById(id);
    }

    private Worker getWorkerOrThrow(Long id) {
        return workerRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));
    }

    private Store getStoreOrThrow(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));
    }
}