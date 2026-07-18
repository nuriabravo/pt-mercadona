package com.mercadona.nuriabravo.infrastructure.controller;

import com.mercadona.nuriabravo.application.dto.input.AssignmentRequestDto;
import com.mercadona.nuriabravo.application.dto.output.AssignmentResponseDto;
import com.mercadona.nuriabravo.application.service.WorkerSectionAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/workers/{workerId}/assignments")
@RequiredArgsConstructor
@Tag(name = "Worker Section Assignments", description = "Asignación de trabajadores a secciones")
public class WorkerSectionAssignmentController {

    private final WorkerSectionAssignmentService assignmentService;

    @GetMapping
    @Operation(summary = "Listar las asignaciones de un trabajador")
    public ResponseEntity<List<AssignmentResponseDto>> findByWorker(@PathVariable Long workerId) {
        return ResponseEntity.ok(assignmentService.findByWorker(workerId));
    }

    @PostMapping
    @Operation(summary = "Asignar a un trabajador a una sección durante N horas")
    public ResponseEntity<AssignmentResponseDto> assign(
            @PathVariable Long workerId,
            @Valid @RequestBody AssignmentRequestDto dto) {
        AssignmentResponseDto created = assignmentService.assign(workerId, dto);
        return ResponseEntity.created(
                URI.create("/api/workers/" + workerId + "/assignments/" + created.getId())
        ).body(created);
    }

    @DeleteMapping("/{assignmentId}")
    @Operation(summary = "Desasignar a un trabajador de una sección")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unassign(@PathVariable Long workerId, @PathVariable Long assignmentId) {
        assignmentService.unassign(workerId, assignmentId);
    }
}