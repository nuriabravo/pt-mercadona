package com.mercadona.nuriabravo.application.service.impl;

import com.mercadona.nuriabravo.application.dto.input.AssignmentRequestDto;
import com.mercadona.nuriabravo.application.dto.output.AssignmentResponseDto;
import com.mercadona.nuriabravo.domain.exception.AssignmentNotFoundException;
import com.mercadona.nuriabravo.domain.exception.SectionNotFoundException;
import com.mercadona.nuriabravo.domain.exception.WorkerHoursExceededException;
import com.mercadona.nuriabravo.domain.exception.WorkerNotFoundException;
import com.mercadona.nuriabravo.domain.mapper.WorkerSectionAssignmentMapper;
import com.mercadona.nuriabravo.domain.model.Section;
import com.mercadona.nuriabravo.domain.model.Store;
import com.mercadona.nuriabravo.domain.model.StoreSection;
import com.mercadona.nuriabravo.domain.model.Worker;
import com.mercadona.nuriabravo.domain.model.WorkerSectionAssignment;
import com.mercadona.nuriabravo.domain.repository.SectionRepository;
import com.mercadona.nuriabravo.domain.repository.StoreSectionRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerSectionAssignmentServiceTest {

    @Mock
    private WorkerRepository workerRepository;
    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private StoreSectionRepository storeSectionRepository;
    @Mock
    private WorkerSectionAssignmentRepository assignmentRepository;
    @Mock
    private WorkerSectionAssignmentMapper mapper;

    @InjectMocks
    private WorkerSectionAssignmentServiceImpl service;

    @Test
    void assign_shouldThrowWorkerHoursExceededException_whenHoursExceedContract() {
        Worker worker = Worker.builder()
                .id(1L)
                .contractHours(4)
                .store(Store.builder().id(1L).build())
                .build();
        Section section = Section.builder().id(1L).name("Horno").build();
        AssignmentRequestDto dto = new AssignmentRequestDto(1L, 5);

        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));
        when(sectionRepository.findById(1L)).thenReturn(Optional.of(section));
        when(storeSectionRepository.findByStoreIdAndSectionId(1L, 1L))
                .thenReturn(Optional.of(StoreSection.builder().id(1L).build()));
        when(assignmentRepository.findByWorkerId(1L)).thenReturn(List.of());

        assertThrows(WorkerHoursExceededException.class, () -> service.assign(1L, dto));
    }

    @Test
    void assign_shouldCreateAssignment_whenHoursAreWithinContract() {
        Store store = Store.builder().id(1L).build();
        Worker worker = Worker.builder().id(1L).contractHours(8).store(store).build();
        Section section = Section.builder().id(1L).name("Horno").build();
        StoreSection storeSection = StoreSection.builder().id(1L).store(store).section(section).build();
        AssignmentRequestDto dto = new AssignmentRequestDto(1L, 4);

        WorkerSectionAssignment savedAssignment = WorkerSectionAssignment.builder()
                .id(10L)
                .worker(worker)
                .storeSection(storeSection)
                .assignedHours(4)
                .build();
        AssignmentResponseDto expectedDto = AssignmentResponseDto.builder()
                .id(10L)
                .workerId(1L)
                .sectionName("Horno")
                .assignedHours(4)
                .build();

        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));
        when(sectionRepository.findById(1L)).thenReturn(Optional.of(section));
        when(storeSectionRepository.findByStoreIdAndSectionId(1L, 1L))
                .thenReturn(Optional.of(storeSection));
        when(assignmentRepository.findByWorkerId(1L)).thenReturn(List.of());
        when(assignmentRepository.save(any(WorkerSectionAssignment.class))).thenReturn(savedAssignment);
        when(mapper.toResponseDto(savedAssignment)).thenReturn(expectedDto);

        AssignmentResponseDto result = service.assign(1L, dto);

        assertThat(result).isEqualTo(expectedDto);
        verify(assignmentRepository).save(any(WorkerSectionAssignment.class));
    }

    @Test
    void assign_shouldCreateStoreSection_whenItDoesNotExistYet() {
        Store store = Store.builder().id(1L).build();
        Worker worker = Worker.builder().id(1L).contractHours(8).store(store).build();
        Section section = Section.builder().id(2L).name("Cajas").build();
        StoreSection newStoreSection = StoreSection.builder().id(5L).store(store).section(section).build();
        AssignmentRequestDto dto = new AssignmentRequestDto(2L, 3);

        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));
        when(sectionRepository.findById(2L)).thenReturn(Optional.of(section));
        when(storeSectionRepository.findByStoreIdAndSectionId(1L, 2L))
                .thenReturn(Optional.empty());
        when(storeSectionRepository.save(any(StoreSection.class))).thenReturn(newStoreSection);
        when(assignmentRepository.findByWorkerId(1L)).thenReturn(List.of());
        when(assignmentRepository.save(any(WorkerSectionAssignment.class)))
                .thenReturn(WorkerSectionAssignment.builder().id(20L).build());
        when(mapper.toResponseDto(any())).thenReturn(AssignmentResponseDto.builder().id(20L).build());

        service.assign(1L, dto);

        verify(storeSectionRepository).save(any(StoreSection.class));
    }

    @Test
    void assign_shouldThrowWorkerNotFoundException_whenWorkerDoesNotExist() {
        AssignmentRequestDto dto = new AssignmentRequestDto(1L, 3);
        when(workerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkerNotFoundException.class, () -> service.assign(1L, dto));
        verifyNoInteractions(sectionRepository, storeSectionRepository, assignmentRepository);
    }

    @Test
    void assign_shouldThrowSectionNotFoundException_whenSectionDoesNotExist() {
        Worker worker = Worker.builder().id(1L).contractHours(8).store(Store.builder().id(1L).build()).build();
        AssignmentRequestDto dto = new AssignmentRequestDto(99L, 3);

        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));
        when(sectionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SectionNotFoundException.class, () -> service.assign(1L, dto));
    }

    @Test
    void findByWorker_shouldReturnMappedAssignments() {
        WorkerSectionAssignment assignment = WorkerSectionAssignment.builder().id(1L).assignedHours(4).build();
        AssignmentResponseDto responseDto = AssignmentResponseDto.builder().id(1L).assignedHours(4).build();

        when(assignmentRepository.findByWorkerId(1L)).thenReturn(List.of(assignment));
        when(mapper.toResponseDto(assignment)).thenReturn(responseDto);

        List<AssignmentResponseDto> result = service.findByWorker(1L);

        assertThat(result).containsExactly(responseDto);
    }

    @Test
    void findByWorker_shouldReturnEmptyList_whenWorkerHasNoAssignments() {
        when(assignmentRepository.findByWorkerId(1L)).thenReturn(List.of());

        List<AssignmentResponseDto> result = service.findByWorker(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void unassign_shouldDeleteAssignment_whenItBelongsToWorker() {
        Worker worker = Worker.builder().id(1L).build();
        WorkerSectionAssignment assignment = WorkerSectionAssignment.builder()
                .id(10L)
                .worker(worker)
                .build();

        when(assignmentRepository.findById(10L)).thenReturn(Optional.of(assignment));

        service.unassign(1L, 10L);

        verify(assignmentRepository).deleteById(10L);
    }

    @Test
    void unassign_shouldThrowAssignmentNotFoundException_whenAssignmentDoesNotExist() {
        when(assignmentRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(AssignmentNotFoundException.class, () -> service.unassign(1L, 10L));
        verify(assignmentRepository, never()).deleteById(any());
    }

    @Test
    void unassign_shouldThrowAssignmentNotFoundException_whenAssignmentBelongsToAnotherWorker() {
        Worker anotherWorker = Worker.builder().id(2L).build();
        WorkerSectionAssignment assignment = WorkerSectionAssignment.builder()
                .id(10L)
                .worker(anotherWorker)
                .build();

        when(assignmentRepository.findById(10L)).thenReturn(Optional.of(assignment));

        assertThrows(AssignmentNotFoundException.class, () -> service.unassign(1L, 10L));
        verify(assignmentRepository, never()).deleteById(any());
    }
}