package com.mercadona.nuriabravo.application.service.impl;

import com.mercadona.nuriabravo.application.dto.output.AssignedWorkerDto;
import com.mercadona.nuriabravo.application.dto.output.SectionStatusDto;
import com.mercadona.nuriabravo.application.dto.output.StoreStatusReportDto;
import com.mercadona.nuriabravo.application.service.StoreReportService;
import com.mercadona.nuriabravo.domain.exception.StoreNotFoundException;
import com.mercadona.nuriabravo.domain.mapper.StoreReportMapper;
import com.mercadona.nuriabravo.domain.model.Store;
import com.mercadona.nuriabravo.domain.model.StoreSection;
import com.mercadona.nuriabravo.domain.repository.StoreRepository;
import com.mercadona.nuriabravo.domain.repository.StoreSectionRepository;
import com.mercadona.nuriabravo.domain.repository.WorkerSectionAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreReportServiceImpl implements StoreReportService {

    private final StoreRepository storeRepository;
    private final StoreSectionRepository storeSectionRepository;
    private final WorkerSectionAssignmentRepository assignmentRepository;
    private final StoreReportMapper mapper;

    @Override
    public StoreStatusReportDto getStoreStatus(Long storeId) {
        Store store = getStoreOrThrow(storeId);
        List<StoreSection> storeSections = storeSectionRepository.findByStoreId(storeId);

        List<SectionStatusDto> sectionStatuses = storeSections.stream()
                .map(this::buildSectionStatus)
                .toList();

        return mapper.toStoreStatusReportDto(store, sectionStatuses);
    }

    private SectionStatusDto buildSectionStatus(StoreSection storeSection) {
        List<AssignedWorkerDto> assignedWorkers = mapper.toAssignedWorkerDtoList(
                assignmentRepository.findByStoreSectionId(storeSection.getId())
        );
        return mapper.toSectionStatusDto(storeSection, assignedWorkers);
    }

    private Store getStoreOrThrow(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));
    }
}