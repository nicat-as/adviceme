package com.uniso.equso.service.impl;

import com.uniso.equso.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final ConcurrentHashMap<Long, CopyOnWriteArrayList<String>> tokenCache;

    public TokenServiceImpl(ConcurrentHashMap<Long, CopyOnWriteArrayList<String>> tokenCache) {
        this.tokenCache = tokenCache;
    }

    public void addToken(Long userId, String token) {
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

    public boolean validateToken(Long userId, String token) {
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

    public boolean removeToken(Long userId, String token) {
        return tokenCache.get(userId)
                .remove(token);
    }

    public void removeUsers(List<Long> userList) {
        userList.forEach(tokenCache::remove);
    }

    @Override
    public ConcurrentHashMap<Long, CopyOnWriteArrayList<String>> getTokenCache() {
        return this.tokenCache;
    }


}
