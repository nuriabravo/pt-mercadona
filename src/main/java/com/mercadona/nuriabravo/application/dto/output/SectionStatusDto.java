package com.mercadona.nuriabravo.application.dto.output;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionStatusDto {
    private String sectionName;
    private List<AssignedWorkerDto> assignedWorkers;
}