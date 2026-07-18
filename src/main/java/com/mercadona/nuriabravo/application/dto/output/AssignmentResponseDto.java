package com.mercadona.nuriabravo.application.dto.output;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentResponseDto {
    private Long id;
    private Long workerId;
    private String sectionName;
    private Integer assignedHours;
}