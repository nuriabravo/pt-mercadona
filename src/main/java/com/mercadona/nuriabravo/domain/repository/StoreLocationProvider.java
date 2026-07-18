package com.mercadona.nuriabravo.domain.repository;

import java.util.Optional;

public interface StoreLocationProvider {
    Optional<String> findAddressByStoreId(Long storeId);
}