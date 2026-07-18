package com.mercadona.nuriabravo.application.dto.output;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreHoursReportDto {
    private String storeName;
    private List<RemainderSectionDto> remainderSections;
    private String storeAddress;
}
