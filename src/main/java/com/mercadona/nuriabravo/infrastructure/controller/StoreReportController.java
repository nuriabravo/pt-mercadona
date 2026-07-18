package com.mercadona.nuriabravo.infrastructure.controller;

import com.mercadona.nuriabravo.application.dto.output.StoreStatusReportDto;
import com.mercadona.nuriabravo.application.service.StoreReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores/{storeId}/reports")
@RequiredArgsConstructor
@Tag(name = "Store Reports", description = "Informes sobre el estado de una tienda")
public class StoreReportController {

    private final StoreReportService storeReportService;

    @GetMapping("/status")
    @Operation(summary = "Estado de una tienda: secciones y trabajadores asignados")
    public ResponseEntity<StoreStatusReportDto> getStoreStatus(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeReportService.getStoreStatus(storeId));
    }
}