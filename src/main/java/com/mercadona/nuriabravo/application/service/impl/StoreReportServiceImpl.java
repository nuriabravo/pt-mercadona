package com.mercadona.nuriabravo.application.service.impl;

import com.mercadona.nuriabravo.application.dto.output.*;
import com.mercadona.nuriabravo.application.service.StoreReportService;
import com.mercadona.nuriabravo.domain.exception.StoreNotFoundException;
import com.mercadona.nuriabravo.domain.mapper.StoreReportMapper;
import com.mercadona.nuriabravo.domain.model.Store;
import com.mercadona.nuriabravo.domain.model.StoreSection;
import com.mercadona.nuriabravo.domain.model.WorkerSectionAssignment;
import com.mercadona.nuriabravo.domain.repository.StoreLocationProvider;
import com.mercadona.nuriabravo.domain.repository.StoreRepository;
import com.mercadona.nuriabravo.domain.repository.StoreSectionRepository;
import com.mercadona.nuriabravo.domain.repository.WorkerSectionAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreReportServiceImpl implements StoreReportService {

    private final StoreRepository storeRepository;
    private final StoreSectionRepository storeSectionRepository;
    private final WorkerSectionAssignmentRepository assignmentRepository;
    private final StoreReportMapper mapper;
    private final StoreLocationProvider storeLocationProvider;

    @Override
    public StoreStatusReportDto getStoreStatus(Long storeId) {
        Store store = getStoreOrThrow(storeId);
        List<StoreSection> storeSections = storeSectionRepository.findByStoreId(storeId);

        List<SectionStatusDto> sectionStatuses = storeSections.stream()
                .map(this::buildSectionStatus)
                .toList();

        String address = resolveStoreAddress(storeId);

        return mapper.toStoreStatusReportDto(store, address, sectionStatuses);
    }

    @Override
    public StoreHoursReportDto getUncoveredHours(Long storeId) {
        Store store = getStoreOrThrow(storeId);
        List<StoreSection> storeSections = storeSectionRepository.findByStoreId(storeId);

        List<RemainderSectionDto> remainderSections = storeSections.stream()
                .map(this::buildRemainderSection)
                .filter(Objects::nonNull)
                .toList();

        String address = resolveStoreAddress(storeId);

        return mapper.toStoreHoursReportDto(store, address, remainderSections);
    }

    private String resolveStoreAddress(Long storeId) {
        return storeLocationProvider.findAddressByStoreId(storeId).orElse(null);
    }

    private SectionStatusDto buildSectionStatus(StoreSection storeSection) {
        List<AssignedWorkerDto> assignedWorkers = mapper.toAssignedWorkerDtoList(
                assignmentRepository.findByStoreSectionId(storeSection.getId())
        );
        return mapper.toSectionStatusDto(storeSection, assignedWorkers);
    }

    private RemainderSectionDto buildRemainderSection(StoreSection storeSection) {
        int assignedHours = sumAssignedHours(storeSection.getId());
        int requiredHours = storeSection.getSection().getDailyRequiredHours();
        int missingHours = requiredHours - assignedHours;

        if (missingHours <= 0) {
            return null;
        }

        return mapper.toRemainderSectionDto(storeSection, missingHours);
    }

    private int sumAssignedHours(Long storeSectionId) {
        return assignmentRepository.findByStoreSectionId(storeSectionId).stream()
                .mapToInt(WorkerSectionAssignment::getAssignedHours)
                .sum();
    }

    private Store getStoreOrThrow(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));
    }
}