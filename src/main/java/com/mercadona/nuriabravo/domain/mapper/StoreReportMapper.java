package com.mercadona.nuriabravo.domain.mapper;

import com.mercadona.nuriabravo.application.dto.output.*;
import com.mercadona.nuriabravo.domain.model.Skill;
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
    @Mapping(target = "storeAddress", source = "storeAddress")
    StoreStatusReportDto toStoreStatusReportDto(Store store, String storeAddress, List<SectionStatusDto> sections);

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
    @Mapping(target = "storeAddress", source = "storeAddress")
    StoreHoursReportDto toStoreHoursReportDto(Store store, String storeAddress, List<RemainderSectionDto> remainderSections);

    @Mapping(target = "sectionName", source = "storeSection.section.name")
    @Mapping(target = "missingHours", source = "missingHours")
    RemainderSectionDto toRemainderSectionDto(StoreSection storeSection, Integer missingHours);

    @Mapping(target = "storeName", source = "store.name")
    @Mapping(target = "sections", source = "sections")
    StoreSkillsReportDto toStoreSkillsReportDto(Store store, List<SectionSkillsDto> sections);

    @Mapping(target = "sectionName", source = "storeSection.section.name")
    @Mapping(target = "requiredSkills", source = "requiredSkills")
    SectionSkillsDto toSectionSkillsDto(StoreSection storeSection, List<SkillDto> requiredSkills);

    @Mapping(target = "name", source = "skill.name")
    @Mapping(target = "description", source = "skill.description")
    SkillDto toSkillDto(Skill skill);

    List<SkillDto> toSkillDtoList(List<Skill> skills);
}