package com.mercadona.nuriabravo.application.dto.output;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private Integer contractHours;
    private StoreResponseDto store;
}