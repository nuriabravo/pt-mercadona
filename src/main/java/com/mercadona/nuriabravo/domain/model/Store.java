package com.mercadona.nuriabravo.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {
    private Long id;
    private String code;
    private String name;
}