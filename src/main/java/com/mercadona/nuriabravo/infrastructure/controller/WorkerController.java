package com.mercadona.nuriabravo.infrastructure.controller;

import com.mercadona.nuriabravo.application.dto.input.WorkerRequestDto;
import com.mercadona.nuriabravo.application.dto.output.WorkerResponseDto;
import com.mercadona.nuriabravo.application.service.WorkerService;
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
@RequestMapping("/api/workers")
@RequiredArgsConstructor
@Tag(name = "Workers", description = "Gestión de trabajadores")
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping
    @Operation(summary = "Listar todos los trabajadores")
    public ResponseEntity<List<WorkerResponseDto>> findAll() {
        return ResponseEntity.ok(workerService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un trabajador por su identificador")
    public ResponseEntity<WorkerResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(workerService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear un trabajador")
    public ResponseEntity<WorkerResponseDto> create(@Valid @RequestBody WorkerRequestDto dto) {
        WorkerResponseDto created = workerService.create(dto);
        return ResponseEntity.created(URI.create("/api/workers/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un trabajador")
    public ResponseEntity<WorkerResponseDto> update(@PathVariable Long id, @Valid @RequestBody WorkerRequestDto dto) {
        return ResponseEntity.ok(workerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un trabajador")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        workerService.delete(id);
    }
}