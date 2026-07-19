package com.mercadona.nuriabravo.infrastructure.controller;

import com.mercadona.nuriabravo.application.dto.output.StoreHoursReportDto;
import com.mercadona.nuriabravo.application.dto.output.StoreSkillsReportDto;
import com.mercadona.nuriabravo.application.dto.output.StoreStatusReportDto;
import com.mercadona.nuriabravo.application.service.StoreReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Tag(name = "Store Reports", description = "Informes sobre el estado de una tienda")
public class StoreReportController {

    private final StoreReportService storeReportService;

    @GetMapping("/{storeId}/reports/status")
    @Operation(summary = "Estado de una tienda: secciones y trabajadores asignados")
    public ResponseEntity<StoreStatusReportDto> getStoreStatus(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeReportService.getStoreStatus(storeId));
    }

    @GetMapping("/{storeId}/reports/uncovered-hours")
    @Operation(summary = "Secciones con horas sin cubrir en una tienda")
    public ResponseEntity<StoreHoursReportDto> getUncoveredHours(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeReportService.getUncoveredHours(storeId));
    }

    @GetMapping("/{storeCode}/reports/skills")
    @Operation(summary = "Aptitudes requeridas por las secciones de una tienda, buscada por código")
    public ResponseEntity<StoreSkillsReportDto> getStoreSkillsByCode(@PathVariable String storeCode) {
        return ResponseEntity.ok(storeReportService.getStoreSkillsByCode(storeCode));
    }
}