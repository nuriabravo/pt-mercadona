package com.mercadona.nuriabravo.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreSection {
    private Long id;
    private Store store;
    private Section section;
}