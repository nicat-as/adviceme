package com.uniso.equso.exceptions;

public class PostException extends BaseException {
    private static final String MESSAGE = "POST NOT CREATED";

    public PostException(String code, String message) {
        super(code, message);
    }
    public PostException(String code){
        super(code,MESSAGE);
    }
}
