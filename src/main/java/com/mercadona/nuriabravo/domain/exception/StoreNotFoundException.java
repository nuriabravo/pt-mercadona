package com.mercadona.nuriabravo.domain.exception;

public class StoreNotFoundException extends RuntimeException {
    public StoreNotFoundException(Long id) {
        super("store.notFound:" + id);
    }
}