package com.mercadona.nuriabravo.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Worker {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private Integer contractHours;
    private Store store;
}