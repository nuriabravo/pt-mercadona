package com.mercadona.nuriabravo.domain.exception;

public class SectionNotFoundException extends RuntimeException {
    public SectionNotFoundException(Long id) {
        super("section.notFound:" + id);
    }
}