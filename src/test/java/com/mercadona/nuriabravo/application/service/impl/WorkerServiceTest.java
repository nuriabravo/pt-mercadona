package com.mercadona.nuriabravo.application.service.impl;

import com.mercadona.nuriabravo.application.dto.input.WorkerRequestDto;
import com.mercadona.nuriabravo.application.dto.output.WorkerResponseDto;
import com.mercadona.nuriabravo.domain.exception.StoreNotFoundException;
import com.mercadona.nuriabravo.domain.exception.WorkerHoursExceededException;
import com.mercadona.nuriabravo.domain.exception.WorkerNotFoundException;
import com.mercadona.nuriabravo.domain.mapper.WorkerMapper;
import com.mercadona.nuriabravo.domain.model.Store;
import com.mercadona.nuriabravo.domain.model.Worker;
import com.mercadona.nuriabravo.domain.model.WorkerSectionAssignment;
import com.mercadona.nuriabravo.domain.repository.StoreRepository;
import com.mercadona.nuriabravo.domain.repository.WorkerRepository;
import com.mercadona.nuriabravo.domain.repository.WorkerSectionAssignmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerServiceTest {

    @Mock
    private WorkerRepository workerRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private WorkerMapper workerMapper;
    @Mock
    private WorkerSectionAssignmentRepository assignmentRepository;

    @InjectMocks
    private WorkerServiceImpl service;

    @Test
    void findAll_shouldReturnMappedWorkers() {
        Worker worker = Worker.builder().id(1L).firstName("Laura").build();
        WorkerResponseDto dto = WorkerResponseDto.builder().id(1L).firstName("Laura").build();

        when(workerRepository.findAll()).thenReturn(List.of(worker));
        when(workerMapper.toResponseDto(worker)).thenReturn(dto);

        List<WorkerResponseDto> result = service.findAll();

        assertThat(result).containsExactly(dto);
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoWorkersExist() {
        when(workerRepository.findAll()).thenReturn(List.of());

        List<WorkerResponseDto> result = service.findAll();

        assertThat(result).isEmpty();
    }


    @Test
    void findById_shouldReturnMappedWorker_whenExists() {
        Worker worker = Worker.builder().id(1L).firstName("Laura").build();
        WorkerResponseDto dto = WorkerResponseDto.builder().id(1L).firstName("Laura").build();

        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));
        when(workerMapper.toResponseDto(worker)).thenReturn(dto);

        WorkerResponseDto result = service.findById(1L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void findById_shouldThrowWorkerNotFoundException_whenNotExists() {
        when(workerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkerNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void create_shouldSaveWorkerWithStore_whenStoreExists() {
        Store store = Store.builder().id(1L).name("Mercadona Madrid").build();
        WorkerRequestDto requestDto = new WorkerRequestDto("Laura", "Gómez", "12345678Z", 8, 1L);
        Worker workerToSave = Worker.builder().firstName("Laura").lastName("Gómez").dni("12345678Z").contractHours(8).build();
        Worker savedWorker = Worker.builder().id(1L).firstName("Laura").store(store).build();
        WorkerResponseDto responseDto = WorkerResponseDto.builder().id(1L).firstName("Laura").build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(workerMapper.toDomain(requestDto)).thenReturn(workerToSave);
        when(workerRepository.save(workerToSave)).thenReturn(savedWorker);
        when(workerMapper.toResponseDto(savedWorker)).thenReturn(responseDto);

        WorkerResponseDto result = service.create(requestDto);

        assertThat(result).isEqualTo(responseDto);
        assertThat(workerToSave.getStore()).isEqualTo(store);
        verify(workerRepository).save(workerToSave);
    }

    @Test
    void create_shouldThrowStoreNotFoundException_whenStoreDoesNotExist() {
        WorkerRequestDto requestDto = new WorkerRequestDto("Laura", "Gómez", "12345678Z", 8, 99L);

        when(storeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StoreNotFoundException.class, () -> service.create(requestDto));
        verifyNoInteractions(workerMapper);
        verify(workerRepository, never()).save(any());
    }

    @Test
    void update_shouldUpdateWorker_whenContractHoursCoverAssignedHours() {
        Store store = Store.builder().id(1L).build();
        Worker existingWorker = Worker.builder().id(1L).contractHours(8).build();
        WorkerRequestDto requestDto = new WorkerRequestDto("Laura", "Gómez", "12345678Z", 6, 1L);
        WorkerSectionAssignment assignment = WorkerSectionAssignment.builder().assignedHours(4).build();
        WorkerResponseDto responseDto = WorkerResponseDto.builder().id(1L).contractHours(6).build();

        when(workerRepository.findById(1L)).thenReturn(Optional.of(existingWorker));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(assignmentRepository.findByWorkerId(1L)).thenReturn(List.of(assignment));
        when(workerRepository.save(existingWorker)).thenReturn(existingWorker);
        when(workerMapper.toResponseDto(existingWorker)).thenReturn(responseDto);

        WorkerResponseDto result = service.update(1L, requestDto);

        assertThat(result).isEqualTo(responseDto);
        verify(workerMapper).updateDomainFromDto(requestDto, existingWorker);
        assertThat(existingWorker.getStore()).isEqualTo(store);
    }

    @Test
    void update_shouldThrowWorkerHoursExceededException_whenNewContractHoursBelowAssigned() {
        Store store = Store.builder().id(1L).build();
        Worker existingWorker = Worker.builder().id(1L).contractHours(8).build();
        WorkerRequestDto requestDto = new WorkerRequestDto("Laura", "Gómez", "12345678Z", 2, 1L);
        WorkerSectionAssignment assignment = WorkerSectionAssignment.builder().assignedHours(4).build();

        when(workerRepository.findById(1L)).thenReturn(Optional.of(existingWorker));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(assignmentRepository.findByWorkerId(1L)).thenReturn(List.of(assignment));

        assertThrows(WorkerHoursExceededException.class, () -> service.update(1L, requestDto));
        verify(workerRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowWorkerNotFoundException_whenWorkerDoesNotExist() {
        WorkerRequestDto requestDto = new WorkerRequestDto("Laura", "Gómez", "12345678Z", 6, 1L);

        when(workerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkerNotFoundException.class, () -> service.update(1L, requestDto));
        verifyNoInteractions(storeRepository, assignmentRepository);
    }

    @Test
    void update_shouldThrowStoreNotFoundException_whenStoreDoesNotExist() {
        Worker existingWorker = Worker.builder().id(1L).contractHours(8).build();
        WorkerRequestDto requestDto = new WorkerRequestDto("Laura", "Gómez", "12345678Z", 6, 99L);

        when(workerRepository.findById(1L)).thenReturn(Optional.of(existingWorker));
        when(storeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StoreNotFoundException.class, () -> service.update(1L, requestDto));
    }

    @Test
    void delete_shouldRemoveWorker_whenExists() {
        when(workerRepository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(workerRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowWorkerNotFoundException_whenNotExists() {
        when(workerRepository.existsById(1L)).thenReturn(false);

        assertThrows(WorkerNotFoundException.class, () -> service.delete(1L));
        verify(workerRepository, never()).deleteById(any());
    }
}