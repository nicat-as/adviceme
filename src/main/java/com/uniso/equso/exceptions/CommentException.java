package com.uniso.equso.exceptions;

public class CommentException extends BaseException {
    private static final String MESSAGE = "Requested comment action failed";

    public CommentException(String code){
        super(code,MESSAGE);
    }

    public CommentException(String code, String message) {
        super(code, message);
    }
}
