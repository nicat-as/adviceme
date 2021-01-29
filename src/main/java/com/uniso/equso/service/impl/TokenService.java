package com.uniso.equso.service.impl;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class TokenService {

    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<String>> tokenCache = new ConcurrentHashMap<>();

    public static void addToken(Long userId, String token) {
        log.info("ActionLog.addToken.start");
        var cacheList = tokenCache.get(userId);
        if (cacheList == null || cacheList.isEmpty()) {
            tokenCache.put(userId, new CopyOnWriteArrayList<>(List.of(token)));
        } else {
            log.debug("Get cache list :{}", cacheList.size());
            cacheList.add(token);
            tokenCache.put(userId, cacheList);
        }
        log.info("ActionLog.addToken.ended");
    }

    public static boolean validateToken(Long userId, String token) {
        log.info("ActionLog.validateToken.start");
        boolean isValid = false;
        var tokenList = tokenCache.get(userId);
        if (tokenList == null ||
                tokenList.isEmpty() ||
                !tokenList.contains(token)) {
            isValid = true;
        }
        log.info("ActionLog.validateToken.end");
        return isValid;
    }

    public static ConcurrentHashMap<Long, CopyOnWriteArrayList<String>> getTokenCache() {
        return tokenCache;
    }

    public static boolean remove(Long userId, String token) {
        return tokenCache.get(userId)
                .remove(token);
    }

    public static void removeUsers(List<Long> userList){
        userList.forEach(tokenCache::remove);
    }
}
