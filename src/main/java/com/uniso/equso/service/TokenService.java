package com.uniso.equso.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public interface TokenService {
    void addToken(Long userId, String token);

    boolean validateToken(Long userId, String token);

    boolean removeToken(Long userId, String token);

    void removeUsers(List<Long> userList);

    ConcurrentHashMap<Long, CopyOnWriteArrayList<String>> getTokenCache();
}
