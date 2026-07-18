package com.mercadona.nuriabravo.application.dto.output;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemainderSectionDto {
    private String sectionName;
    private Integer missingHours;
}
