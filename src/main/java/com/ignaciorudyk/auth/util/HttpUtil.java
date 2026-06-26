package com.ignaciorudyk.auth.util;

import com.ignaciorudyk.auth.repository.dto.response.ErrorDTO;
import com.ignaciorudyk.auth.repository.dto.response.MetadataDTO;
import com.ignaciorudyk.auth.repository.dto.response.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;

public class HttpUtil {

    public static ResponseEntity<ResponseDTO> isSucceful2xxResponse(HttpServletRequest httpRequest, Object data) {
        MetadataDTO metadata = new MetadataDTO(httpRequest.getRequestURI(), httpRequest.getMethod(), HttpStatus.OK.value());
        ErrorDTO error = new ErrorDTO();
        error.setCode(0);
        error.setMsg("Operacion completada");
        ResponseDTO response = new ResponseDTO(metadata, data, List.of(error));
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<ResponseDTO> isBadRequestResponse(HttpServletRequest httpRequest, Collection<String> messages) {
        return isFailureRequestResponse(httpRequest, messages, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<ResponseDTO> isFailureRequestResponse(HttpServletRequest httpRequest, Collection<String> messages, HttpStatus httpStatus) {
        MetadataDTO metadata = new MetadataDTO(httpRequest.getRequestURI(), httpRequest.getMethod(), httpStatus.value());
        List<ErrorDTO> errors = messages.stream().map(e -> new ErrorDTO(-1, e)).toList();
        ResponseDTO response = new ResponseDTO(metadata, null, errors);
        return ResponseEntity.status(httpStatus).body(response);
    }

}