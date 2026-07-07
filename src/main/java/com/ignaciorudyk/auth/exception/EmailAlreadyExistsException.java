package com.ignaciorudyk.auth.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends AuthenticationException {

    private static final int CODE = 3;

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    private static final String EMAIL_ALREADY_EXISTS_EXCEPTION_MSG = "El email ya existe";

    public EmailAlreadyExistsException(String message) {
        super(message, CODE, HTTP_STATUS);
    }

    public EmailAlreadyExistsException() {
        this(EMAIL_ALREADY_EXISTS_EXCEPTION_MSG);
    }

}
