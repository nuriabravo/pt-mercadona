package com.mercadona.nuriabravo.domain.mapper;

import com.mercadona.nuriabravo.application.dto.output.*;
import com.mercadona.nuriabravo.domain.model.Store;
import com.mercadona.nuriabravo.domain.model.StoreSection;
import com.mercadona.nuriabravo.domain.model.WorkerSectionAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreReportMapper {

    @Mapping(target = "storeName", source = "store.name")
    @Mapping(target = "sections", source = "sections")
    StoreStatusReportDto toStoreStatusReportDto(Store store, List<SectionStatusDto> sections);

    @Mapping(target = "sectionName", source = "storeSection.section.name")
    @Mapping(target = "assignedWorkers", source = "assignedWorkers")
    SectionStatusDto toSectionStatusDto(StoreSection storeSection, List<AssignedWorkerDto> assignedWorkers);

    @Mapping(target = "firstName", source = "worker.firstName")
    @Mapping(target = "lastName", source = "worker.lastName")
    @Mapping(target = "assignedHours", source = "assignedHours")
    AssignedWorkerDto toAssignedWorkerDto(WorkerSectionAssignment assignment);

    List<AssignedWorkerDto> toAssignedWorkerDtoList(List<WorkerSectionAssignment> assignments);

    @Mapping(target = "storeName", source = "store.name")
    @Mapping(target = "remainderSections", source = "remainderSections")
    StoreHoursReportDto toStoreHoursReportDto(Store store, List<RemainderSectionDto> remainderSections);

    @Mapping(target = "sectionName", source = "storeSection.section.name")
    @Mapping(target = "missingHours", source = "missingHours")
    RemainderSectionDto toRemainderSectionDto(StoreSection storeSection, Integer missingHours);
}