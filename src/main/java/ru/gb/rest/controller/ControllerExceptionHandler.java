package ru.gb.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationErrorHandler(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();

        e.getBindingResult().getAllErrors().stream()
                .map(FieldError.class::cast)
                .forEach(fieldError -> {
                    errors.add(String.format("Bad request %s : %s : Rejected value : %s",
                            fieldError.getField(),
                            fieldError.getDefaultMessage(),
                            fieldError.getRejectedValue()));
                });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
