package com.uniso.equso.config.security;

public interface SecurityConstant {
    long EXPIRATION_TIME = 60 * 60 * 1000; // 1 hour
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
