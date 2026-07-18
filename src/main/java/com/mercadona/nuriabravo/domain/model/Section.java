package com.mercadona.nuriabravo.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {
    private Long id;
    private String name;
    private Integer dailyRequiredHours;
}