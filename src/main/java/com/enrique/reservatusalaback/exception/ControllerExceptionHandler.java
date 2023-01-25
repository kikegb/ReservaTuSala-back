package com.enrique.reservatusalaback.exception;

import com.enrique.reservatusalaback.controller.ResponseCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.management.InstanceAlreadyExistsException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler
        extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> response = new HashMap<>();
        response.put("code", String.valueOf(ResponseCode.BAD_REQUEST.code));
        response.put("description", ResponseCode.BAD_REQUEST.description);
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            response.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { InstanceAlreadyExistsException.class })
    protected ResponseEntity<Object> handleConflict(
            Exception ex, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("code", String.valueOf(ResponseCode.ALREADY_EXISTENT_USER.code));
        response.put("description", ResponseCode.ALREADY_EXISTENT_USER.description);
        return handleExceptionInternal(ex, response,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}