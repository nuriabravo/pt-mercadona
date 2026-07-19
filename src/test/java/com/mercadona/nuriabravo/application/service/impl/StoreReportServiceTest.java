package com.mercadona.nuriabravo.application.service.impl;

import com.mercadona.nuriabravo.application.dto.output.*;
import com.mercadona.nuriabravo.domain.exception.StoreNotFoundException;
import com.mercadona.nuriabravo.domain.mapper.StoreReportMapper;
import com.mercadona.nuriabravo.domain.model.Section;
import com.mercadona.nuriabravo.domain.model.Skill;
import com.mercadona.nuriabravo.domain.model.Store;
import com.mercadona.nuriabravo.domain.model.StoreSection;
import com.mercadona.nuriabravo.domain.model.WorkerSectionAssignment;
import com.mercadona.nuriabravo.domain.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreReportServiceTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private StoreSectionRepository storeSectionRepository;
    @Mock
    private WorkerSectionAssignmentRepository assignmentRepository;
    @Mock
    private StoreReportMapper mapper;
    @Mock
    private StoreLocationProvider storeLocationProvider;
    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private StoreReportServiceImpl service;

    @Test
    void getStoreStatus_shouldBuildReportWithSectionsAndAddress() {
        Store store = Store.builder().id(1L).name("Mercadona Madrid").build();
        Section section = Section.builder().id(1L).name("Horno").build();
        StoreSection storeSection = StoreSection.builder().id(1L).store(store).section(section).build();
        WorkerSectionAssignment assignment = WorkerSectionAssignment.builder().assignedHours(4).build();

        AssignedWorkerDto assignedWorkerDto = AssignedWorkerDto.builder().firstName("Laura").build();
        SectionStatusDto sectionStatusDto = SectionStatusDto.builder().sectionName("Horno").build();
        StoreStatusReportDto expected = StoreStatusReportDto.builder().storeName("Mercadona Madrid").build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(storeSectionRepository.findByStoreId(1L)).thenReturn(List.of(storeSection));
        when(assignmentRepository.findByStoreSectionId(1L)).thenReturn(List.of(assignment));
        when(mapper.toAssignedWorkerDtoList(List.of(assignment))).thenReturn(List.of(assignedWorkerDto));
        when(mapper.toSectionStatusDto(storeSection, List.of(assignedWorkerDto))).thenReturn(sectionStatusDto);
        when(storeLocationProvider.findAddressByStoreId(1L)).thenReturn(Optional.of("Calle Falsa 123"));
        when(mapper.toStoreStatusReportDto(store, "Calle Falsa 123", List.of(sectionStatusDto)))
                .thenReturn(expected);

        StoreStatusReportDto result = service.getStoreStatus(1L);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getStoreStatus_shouldUseNullAddress_whenLocationProviderReturnsEmpty() {
        Store store = Store.builder().id(1L).name("Mercadona Madrid").build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(storeSectionRepository.findByStoreId(1L)).thenReturn(List.of());
        when(storeLocationProvider.findAddressByStoreId(1L)).thenReturn(Optional.empty());
        when(mapper.toStoreStatusReportDto(store, null, List.of()))
                .thenReturn(StoreStatusReportDto.builder().storeName("Mercadona Madrid").build());

        service.getStoreStatus(1L);

        verify(mapper).toStoreStatusReportDto(store, null, List.of());
    }

    @Test
    void getStoreStatus_shouldThrowStoreNotFoundException_whenStoreDoesNotExist() {
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StoreNotFoundException.class, () -> service.getStoreStatus(1L));
        verifyNoInteractions(storeSectionRepository, assignmentRepository, storeLocationProvider);
    }

    @Test
    void getUncoveredHours_shouldIncludeOnlySectionsWithMissingHours() {
        Store store = Store.builder().id(1L).name("Mercadona Madrid").build();
        Section coveredSection = Section.builder().id(1L).name("Horno").dailyRequiredHours(8).build();
        Section uncoveredSection = Section.builder().id(2L).name("Cajas").dailyRequiredHours(16).build();
        StoreSection coveredStoreSection = StoreSection.builder().id(1L).store(store).section(coveredSection).build();
        StoreSection uncoveredStoreSection = StoreSection.builder().id(2L).store(store).section(uncoveredSection).build();

        WorkerSectionAssignment fullAssignment = WorkerSectionAssignment.builder().assignedHours(8).build();
        WorkerSectionAssignment partialAssignment = WorkerSectionAssignment.builder().assignedHours(4).build();

        RemainderSectionDto remainderDto = RemainderSectionDto.builder().sectionName("Cajas").missingHours(12).build();
        StoreHoursReportDto expected = StoreHoursReportDto.builder().storeName("Mercadona Madrid").build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(storeSectionRepository.findByStoreId(1L))
                .thenReturn(List.of(coveredStoreSection, uncoveredStoreSection));
        when(assignmentRepository.findByStoreSectionId(1L)).thenReturn(List.of(fullAssignment));
        when(assignmentRepository.findByStoreSectionId(2L)).thenReturn(List.of(partialAssignment));
        when(mapper.toRemainderSectionDto(uncoveredStoreSection, 12)).thenReturn(remainderDto);
        when(storeLocationProvider.findAddressByStoreId(1L)).thenReturn(Optional.empty());
        when(mapper.toStoreHoursReportDto(store, null, List.of(remainderDto))).thenReturn(expected);

        StoreHoursReportDto result = service.getUncoveredHours(1L);

        assertThat(result).isEqualTo(expected);
        verify(mapper, never()).toRemainderSectionDto(coveredStoreSection, 0);
    }

    @Test
    void getUncoveredHours_shouldReturnEmptyList_whenAllSectionsAreCovered() {
        Store store = Store.builder().id(1L).build();
        Section section = Section.builder().id(1L).name("Horno").dailyRequiredHours(8).build();
        StoreSection storeSection = StoreSection.builder().id(1L).store(store).section(section).build();
        WorkerSectionAssignment assignment = WorkerSectionAssignment.builder().assignedHours(8).build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(storeSectionRepository.findByStoreId(1L)).thenReturn(List.of(storeSection));
        when(assignmentRepository.findByStoreSectionId(1L)).thenReturn(List.of(assignment));
        when(storeLocationProvider.findAddressByStoreId(1L)).thenReturn(Optional.empty());
        when(mapper.toStoreHoursReportDto(store, null, List.of()))
                .thenReturn(StoreHoursReportDto.builder().build());

        service.getUncoveredHours(1L);

        verify(mapper, never()).toRemainderSectionDto(any(), anyInt());
        verify(mapper).toStoreHoursReportDto(store, null, List.of());
    }

    @Test
    void getStoreSkillsByCode_shouldReturnSkillsForEachSection() {
        Store store = Store.builder().id(1L).code("MAD01").name("Mercadona Madrid").build();
        Section section = Section.builder().id(1L).name("Horno").build();
        StoreSection storeSection = StoreSection.builder().id(1L).store(store).section(section).build();
        Skill skill = Skill.builder().id(1L).name("Hornear Pan").build();

        SkillDto skillDto = SkillDto.builder().name("Hornear Pan").build();
        SectionSkillsDto sectionSkillsDto = SectionSkillsDto.builder().sectionName("Horno").build();
        StoreSkillsReportDto expected = StoreSkillsReportDto.builder().storeName("Mercadona Madrid").build();

        when(storeRepository.findByCode("MAD01")).thenReturn(Optional.of(store));
        when(storeSectionRepository.findByStoreId(1L)).thenReturn(List.of(storeSection));
        when(skillRepository.findBySectionId(1L)).thenReturn(List.of(skill));
        when(mapper.toSkillDtoList(List.of(skill))).thenReturn(List.of(skillDto));
        when(mapper.toSectionSkillsDto(storeSection, List.of(skillDto))).thenReturn(sectionSkillsDto);
        when(mapper.toStoreSkillsReportDto(store, List.of(sectionSkillsDto))).thenReturn(expected);

        StoreSkillsReportDto result = service.getStoreSkillsByCode("MAD01");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getStoreSkillsByCode_shouldThrowStoreNotFoundException_whenCodeDoesNotExist() {
        when(storeRepository.findByCode("unknown")).thenReturn(Optional.empty());

        assertThrows(StoreNotFoundException.class, () -> service.getStoreSkillsByCode("unknown"));
        verifyNoInteractions(storeSectionRepository, skillRepository);
    }
}