package com.mercadona.nuriabravo.domain.exception;

public class WorkerNotFoundException extends RuntimeException {
    public WorkerNotFoundException(Long id) {
        super("worker.notFound:" + id);
    }
}