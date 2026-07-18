package com.mercadona.nuriabravo.application.service;

import com.mercadona.nuriabravo.application.dto.output.StoreStatusReportDto;

public interface StoreReportService {
    StoreStatusReportDto getStoreStatus(Long storeId);
}