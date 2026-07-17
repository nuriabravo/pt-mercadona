package com.mercadona.nuriabravo.domain.mapper;

import com.mercadona.nuriabravo.application.dto.output.StoreResponseDto;
import com.mercadona.nuriabravo.application.dto.input.WorkerRequestDto;
import com.mercadona.nuriabravo.application.dto.output.WorkerResponseDto;
import com.mercadona.nuriabravo.domain.model.Store;
import com.mercadona.nuriabravo.domain.model.Worker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WorkerMapper {

    WorkerResponseDto toResponseDto(Worker worker);

    StoreResponseDto toStoreResponseDto(Store store);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "store", ignore = true)
    Worker toDomain(WorkerRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "store", ignore = true)
    void updateDomainFromDto(WorkerRequestDto dto, @MappingTarget Worker worker);
}