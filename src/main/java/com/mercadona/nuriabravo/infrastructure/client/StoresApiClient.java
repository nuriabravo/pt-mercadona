package com.mercadona.nuriabravo.infrastructure.client;

import com.mercadona.nuriabravo.application.dto.external.ExternalStoreDto;
import com.mercadona.nuriabravo.domain.repository.StoreLocationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Component
@Slf4j
public class StoresApiClient implements StoreLocationProvider {

    private final RestClient restClient;

    public StoresApiClient(@Value("${stores.api.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public Optional<String> findAddressByStoreId(Long storeId) {
        try {
            ExternalStoreDto response = restClient.get()
                    .uri("/stores/{id}", storeId)
                    .retrieve()
                    .body(ExternalStoreDto.class);

            return Optional.ofNullable(response).map(ExternalStoreDto::getAddress);
        } catch (RestClientException ex) {
            log.warn("Could not fetch address for store id {}: {}", storeId, ex.getMessage());
            return Optional.empty();
        }
    }
}