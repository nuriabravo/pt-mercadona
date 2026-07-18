package com.mercadona.nuriabravo.domain.mapper;

import com.mercadona.nuriabravo.application.dto.output.AssignmentResponseDto;
import com.mercadona.nuriabravo.domain.model.WorkerSectionAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkerSectionAssignmentMapper {

    @Mapping(target = "workerId", source = "worker.id")
    @Mapping(target = "sectionName", source = "storeSection.section.name")
    AssignmentResponseDto toResponseDto(WorkerSectionAssignment assignment);
}