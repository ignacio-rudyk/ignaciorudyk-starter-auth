package com.ignaciorudyk.auth.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class AuthenticationException extends RuntimeException {

    private int code;

    private HttpStatus httpStatus;

    private static final String AUTHENTICATION_EXCEPTION_MSG = "Hubo un error en la operacion";

    public AuthenticationException(String message, int code, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public AuthenticationException(String message, int code) {
        this(message, code, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public AuthenticationException(String message) {
        this(message, 1);
    }

    public AuthenticationException() {
        this(AUTHENTICATION_EXCEPTION_MSG);
    }
}
