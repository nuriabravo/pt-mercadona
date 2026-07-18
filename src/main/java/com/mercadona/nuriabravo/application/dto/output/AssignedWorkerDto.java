package com.mercadona.nuriabravo.application.dto.output;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignedWorkerDto {
    private String firstName;
    private String lastName;
    private Integer assignedHours;
}