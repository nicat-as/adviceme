package com.uniso.equso.controller;

import com.uniso.equso.exceptions.AuthenticationException;
import com.uniso.equso.exceptions.PostException;
import com.uniso.equso.model.RestErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public RestErrorResponse handleAuthenticationException(AuthenticationException e) {
        log.error("exception.handleAuthenticationException",e);
        return RestErrorResponse.builder()
                .uuid(e.getUuid())
                .code(e.getCode())
                .message(e.getMessage())
                .status(HttpStatus.FORBIDDEN.name())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code =HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse handleUnknownException(Exception e){
        log.error("exception.handleUnknownException",e);
        return RestErrorResponse.builder()
                .uuid(UUID.randomUUID().toString())
                .code("exception.unknown")
                .message(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .build();
    }

    @ExceptionHandler(PostException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public RestErrorResponse handlePostException(PostException e){
        log.error("exception.handlePostException",e);
        return RestErrorResponse.builder()
                .uuid(e.getUuid())
                .code(e.getCode())
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.name())
                .build();
    }
}
