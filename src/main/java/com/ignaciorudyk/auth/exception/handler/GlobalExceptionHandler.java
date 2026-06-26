package com.ignaciorudyk.auth.exception.handler;

import com.ignaciorudyk.auth.exception.EmailAlreadyExistsException;
import com.ignaciorudyk.auth.exception.InvalidTokenException;
import com.ignaciorudyk.auth.repository.dto.response.ResponseDTO;
import com.ignaciorudyk.auth.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Errores de validación (Bean Validation)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> messages = ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return HttpUtil.isBadRequestResponse(request, messages);
    }

    /**
     * Email ya registrado
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ResponseDTO> handleEmailExists(EmailAlreadyExistsException ex, HttpServletRequest request) {
        return HttpUtil.isFailureRequestResponse(request, List.of(ex.getMessage()), HttpStatus.CONFLICT);
    }

    /**
     * Credenciales inválidas
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDTO> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return HttpUtil.isFailureRequestResponse(request, List.of("Email o contraseña incorrectos"), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Error genérico
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleGenericException(Exception ex, HttpServletRequest request) {
        return HttpUtil.isFailureRequestResponse(request, List.of("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Token inválido o expirado
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ResponseDTO> handleInvalidToken(InvalidTokenException ex, HttpServletRequest request) {
        return HttpUtil.isFailureRequestResponse(request, List.of(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

}
