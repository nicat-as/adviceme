package com.uniso.equso.exceptions;

public class UserException extends BaseException {
    private static final String MESSAGE = "User exception";

    public UserException(String code){
        super(code,MESSAGE);
    }

    public UserException(String code, String message) {
        super(code, message);
    }
}
