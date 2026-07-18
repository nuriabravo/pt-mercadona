package com.mercadona.nuriabravo.application.dto.output;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponseDto {
    private Long id;
    private String code;
    private String name;
}