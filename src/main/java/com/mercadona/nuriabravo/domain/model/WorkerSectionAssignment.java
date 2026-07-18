package com.mercadona.nuriabravo.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerSectionAssignment {
    private Long id;
    private Worker worker;
    private StoreSection storeSection;
    private Integer assignedHours;
}