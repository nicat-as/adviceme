package com.uniso.equso.exceptions;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class BaseException extends RuntimeException {
    private final String uuid;
    private final String code;

    {
        this.uuid = UUID.randomUUID().toString();
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BaseException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

}
