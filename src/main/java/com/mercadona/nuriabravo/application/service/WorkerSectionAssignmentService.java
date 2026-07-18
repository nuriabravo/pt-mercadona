package com.mercadona.nuriabravo.application.service;

import com.mercadona.nuriabravo.application.dto.input.AssignmentRequestDto;
import com.mercadona.nuriabravo.application.dto.output.AssignmentResponseDto;

import java.util.List;

public interface WorkerSectionAssignmentService {
    List<AssignmentResponseDto> findByWorker(Long workerId);
    AssignmentResponseDto assign(Long workerId, AssignmentRequestDto dto);
    void unassign(Long workerId, Long assignmentId);
}