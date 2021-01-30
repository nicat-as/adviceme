package com.uniso.equso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
public class TokenCacheConfig {

    @Bean
    public ConcurrentHashMap<Long, CopyOnWriteArrayList<String>> getTokenCache(){
        return new ConcurrentHashMap<>();
    }
}
