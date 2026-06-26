package com.ignaciorudyk.auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends AuthenticationException {

    private static final int CODE = 2;

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    private static final String INVALID_TOKEN_EXCEPTION_MSG = "El token es invalido";

    public InvalidTokenException(String message) {
        super(message, CODE, HTTP_STATUS);
    }

    public InvalidTokenException() {
        this(INVALID_TOKEN_EXCEPTION_MSG);
    }

}