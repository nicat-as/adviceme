package com.uniso.equso.exceptions;

import lombok.ToString;

@ToString
public class AuthenticationException extends BaseException {
    private static final String MESSAGE = "Authentication failed";

    public AuthenticationException(String code) {
        super(code, MESSAGE);
    }

    public AuthenticationException(String code, String message) {
        super(code, message);
    }
}
