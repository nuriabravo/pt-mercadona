package com.mercadona.nuriabravo.application.service;

import com.mercadona.nuriabravo.application.dto.output.StoreHoursReportDto;
import com.mercadona.nuriabravo.application.dto.output.StoreSkillsReportDto;
import com.mercadona.nuriabravo.application.dto.output.StoreStatusReportDto;

public interface StoreReportService {
    StoreStatusReportDto getStoreStatus(Long storeId);
    StoreHoursReportDto getUncoveredHours(Long storeId);
    StoreSkillsReportDto getStoreSkillsByCode(String storeCode);
}