package com.uniso.equso.controller;

import com.uniso.equso.exceptions.*;
import com.uniso.equso.model.RestErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.*;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public RestErrorResponse<?> handleAuthenticationException(AuthenticationException e) {
        log.error("exception.handleAuthenticationException", e);
        return RestErrorResponse.builder()
                .uuid(e.getUuid())
                .code(e.getCode())
                .message(e.getMessage())
                .status(HttpStatus.FORBIDDEN.name())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse<?> handleUnknownException(Exception e) {
        log.error("exception.handleUnknownException", e);
        return RestErrorResponse.builder()
                .uuid(UUID.randomUUID().toString())
                .code("exception.unknown")
                .message(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .build();
    }

    @ExceptionHandler({PostException.class, CommentException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public RestErrorResponse<?> handlePostException(BaseException e) {
        log.error("exception.handlePostException", e);
        return RestErrorResponse.builder()
                .uuid(e.getUuid())
                .code(e.getCode())
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.name())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestErrorResponse<?> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return RestErrorResponse.builder()
                .uuid(UUID.randomUUID().toString())
                .code("exception.validation-failed")
                .message("Validation failed")
                .status(HttpStatus.BAD_REQUEST.name())
                .error(errors)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public RestErrorResponse<?> handleValidationExceptions(ConstraintViolationException ex) {
        Set<String> errors = new HashSet<>();
        ex.getConstraintViolations()
                .forEach(constraintViolation -> errors.add(constraintViolation.getMessage()));
        return RestErrorResponse.builder()
                .uuid(UUID.randomUUID().toString())
                .code("exception.validation-failed")
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.name())
                .error(errors)
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationException.class)
    public RestErrorResponse<?> handleAuthorizationExceptions(AuthorizationException e){
        log.debug("Authorization exception handling");
        return RestErrorResponse.builder()
                .uuid(e.getUuid())
                .code(e.getCode())
                .message(e.getMessage())
                .status(HttpStatus.FORBIDDEN.name())
                .build();
    }
}
