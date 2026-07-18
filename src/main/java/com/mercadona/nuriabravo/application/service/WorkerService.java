package com.mercadona.nuriabravo.application.service;

import com.mercadona.nuriabravo.application.dto.input.WorkerRequestDto;
import com.mercadona.nuriabravo.application.dto.output.WorkerResponseDto;

import java.util.List;

public interface WorkerService {
    List<WorkerResponseDto> findAll();
    WorkerResponseDto findById(Long id);
    WorkerResponseDto create(WorkerRequestDto dto);
    WorkerResponseDto update(Long id, WorkerRequestDto dto);
    void delete(Long id);
}