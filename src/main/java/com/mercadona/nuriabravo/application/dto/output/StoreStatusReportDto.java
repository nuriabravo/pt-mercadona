package com.mercadona.nuriabravo.application.dto.output;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreStatusReportDto {
    private String storeName;
    private List<SectionStatusDto> sections;
}