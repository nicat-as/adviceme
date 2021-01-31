package com.uniso.equso.exceptions;

public class AuthorizationException extends BaseException {
    private static final String MESSAGE = "You are not authorized to this action";

    public AuthorizationException(String code) {
        super(code, MESSAGE);
    }

    public AuthorizationException(String code, String message) {
        super(code, message);
    }
}
