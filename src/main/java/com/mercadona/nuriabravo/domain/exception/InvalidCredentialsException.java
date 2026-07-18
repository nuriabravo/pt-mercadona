package com.mercadona.nuriabravo.domain.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("auth.invalidCredentials");
    }
}