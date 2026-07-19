package com.mercadona.nuriabravo.application.dto.output;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionSkillsDto {
    private String sectionName;
    private List<SkillDto> requiredSkills;
}