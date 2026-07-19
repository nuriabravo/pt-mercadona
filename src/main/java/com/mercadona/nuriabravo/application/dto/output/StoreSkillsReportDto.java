package com.mercadona.nuriabravo.application.dto.output;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreSkillsReportDto {
    private String storeName;
    private List<SectionSkillsDto> sections;
}