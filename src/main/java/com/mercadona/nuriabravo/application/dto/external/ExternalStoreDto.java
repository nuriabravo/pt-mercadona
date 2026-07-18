package com.mercadona.nuriabravo.application.dto.external;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalStoreDto {
    private Long id;
    private String description;
    private String address;
    private String city;
}