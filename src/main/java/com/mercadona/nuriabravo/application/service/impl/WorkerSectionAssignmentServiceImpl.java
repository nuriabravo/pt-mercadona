package com.mercadona.nuriabravo.application.service.impl;

import com.mercadona.nuriabravo.application.dto.input.AssignmentRequestDto;
import com.mercadona.nuriabravo.application.dto.output.AssignmentResponseDto;
import com.mercadona.nuriabravo.application.service.WorkerSectionAssignmentService;
import com.mercadona.nuriabravo.domain.exception.*;
import com.mercadona.nuriabravo.domain.mapper.WorkerSectionAssignmentMapper;
import com.mercadona.nuriabravo.domain.model.*;
import com.mercadona.nuriabravo.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkerSectionAssignmentServiceImpl implements WorkerSectionAssignmentService {

    private final WorkerRepository workerRepository;
    private final SectionRepository sectionRepository;
    private final StoreSectionRepository storeSectionRepository;
    private final WorkerSectionAssignmentRepository assignmentRepository;
    private final WorkerSectionAssignmentMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponseDto> findByWorker(Long workerId) {
        return assignmentRepository.findByWorkerId(workerId).stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    public AssignmentResponseDto assign(Long workerId, AssignmentRequestDto dto) {
        Worker worker = getWorkerOrThrow(workerId);
        Section section = getSectionOrThrow(dto.getSectionId());
        StoreSection storeSection = getOrCreateStoreSection(worker.getStore(), section);

        validateAvailableHours(worker, dto.getHours());

        WorkerSectionAssignment assignment = buildAssignment(worker, storeSection, dto.getHours());
        return mapper.toResponseDto(assignmentRepository.save(assignment));
    }

    @Override
    public void unassign(Long workerId, Long assignmentId) {
        WorkerSectionAssignment assignment = getAssignmentOrThrow(assignmentId);

        if (!isWorkerAssign(assignment, workerId)) {
            throw new AssignmentNotFoundException(assignmentId);
        }

        assignmentRepository.deleteById(assignmentId);
    }

    private Worker getWorkerOrThrow(Long workerId) {
        return workerRepository.findById(workerId)
                .orElseThrow(() -> new WorkerNotFoundException(workerId));
    }

    private Section getSectionOrThrow(Long sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new SectionNotFoundException(sectionId));
    }

    private WorkerSectionAssignment getAssignmentOrThrow(Long assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException(assignmentId));
    }

    private StoreSection getOrCreateStoreSection(Store store, Section section) {
        return storeSectionRepository.findByStoreIdAndSectionId(store.getId(), section.getId())
                .orElseGet(() -> storeSectionRepository.save(
                        StoreSection.builder()
                                .store(store)
                                .section(section)
                                .build()
                ));
    }

    private void validateAvailableHours(Worker worker, int requestedHours) {
        int currentlyAssigned = sumAssignedHours(worker.getId());
        int totalAfterAssignment = currentlyAssigned + requestedHours;

        if (totalAfterAssignment > worker.getContractHours()) {
            throw new WorkerHoursExceededException(
                    worker.getId(), worker.getContractHours() - currentlyAssigned, requestedHours);
        }
    }

    private int sumAssignedHours(Long workerId) {
        return assignmentRepository.findByWorkerId(workerId).stream()
                .mapToInt(WorkerSectionAssignment::getAssignedHours)
                .sum();
    }

    private WorkerSectionAssignment buildAssignment(Worker worker, StoreSection storeSection, int hours) {
        return WorkerSectionAssignment.builder()
                .worker(worker)
                .storeSection(storeSection)
                .assignedHours(hours)
                .build();
    }

    private boolean isWorkerAssign(WorkerSectionAssignment assignment, Long workerId) {
        return assignment.getWorker().getId().equals(workerId);
    }
}