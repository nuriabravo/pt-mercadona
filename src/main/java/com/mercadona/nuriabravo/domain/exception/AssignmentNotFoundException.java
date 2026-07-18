package com.mercadona.nuriabravo.domain.exception;

public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(Long id) {
        super("assignment.notFound:" + id);
    }
}