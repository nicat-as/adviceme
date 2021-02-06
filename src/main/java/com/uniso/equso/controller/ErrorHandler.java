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

    private static final Locale locale = Locale.forLanguageTag("az");

    private String getLocalizedMessage(String messageKey) {
        return ResourceBundle.getBundle("messages", locale)
                .getString(messageKey);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public RestErrorResponse<?> handleAuthenticationException(AuthenticationException e) {
        log.error("exception.handleAuthenticationException", e);
        return RestErrorResponse.builder()
                .uuid(e.getUuid())
                .code(e.getCode())
                .message(getLocalizedMessage(e.getCode()))
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
                .message(getLocalizedMessage("exception.unknown"))
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
                .message(getLocalizedMessage(e.getCode()))
                .status(HttpStatus.BAD_REQUEST.name())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestErrorResponse<?> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();

                if (errors.containsKey(fieldName)) {
                    errors.get(fieldName).add(errorMessage);
                } else {
                    List<String> list = new ArrayList<>();
                    list.add(errorMessage);
                    errors.put(fieldName, list);
                }
            }
        });

        return RestErrorResponse.builder()
                .uuid(UUID.randomUUID().toString())
                .code("exception.validation-failed")
                .message(getLocalizedMessage("exception.validation-failed"))
                .status(HttpStatus.BAD_REQUEST.name())
                .error(errors)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public RestErrorResponse<?> handleValidationExceptions(ConstraintViolationException ex) {
        Map<String, List<String>> errors = new HashMap<>();

        ex.getConstraintViolations()
                .forEach(constraintViolation -> {
                    var key = constraintViolation.getPropertyPath().toString();
                    if (errors.containsKey(key)) {
                        errors.get(key).add(constraintViolation.getMessage());
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add(constraintViolation.getMessage());
                        errors.put(key, list);
                    }

                });
        return RestErrorResponse.builder()
                .uuid(UUID.randomUUID().toString())
                .code("exception.validation-failed")
                .message(getLocalizedMessage("exception.validation-failed"))
                .status(HttpStatus.BAD_REQUEST.name())
                .error(errors)
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationException.class)
    public RestErrorResponse<?> handleAuthorizationExceptions(AuthorizationException e) {
        log.debug("Authorization exception handling");
        return RestErrorResponse.builder()
                .uuid(e.getUuid())
                .code(e.getCode())
                .message(getLocalizedMessage(e.getCode()))
                .status(HttpStatus.FORBIDDEN.name())
                .build();
    }
}
