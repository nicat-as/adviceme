package com.uniso.equso.scheduler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.uniso.equso.service.impl.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TokenScheduler {

    @Scheduled(cron = "0 */30 * * * *")
    public void removeCache() {
        TokenService.getTokenCache()
                .values()
                .stream()
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .filter(t -> {
                    try {
                        var expireAt = JWT.decode(t).getExpiresAt().getTime();
                        return System.currentTimeMillis() >= expireAt;
                    } catch (JWTDecodeException e) {
                        log.error("exception.jwt-decode-failed", e);
                        return true;
                    }
                })
                .forEach(t -> TokenService.remove(JWT.decode(t).getClaim("id").asLong(), t));

        var listForRemove = TokenService.getTokenCache()
                .entrySet().stream()
                .filter(m -> m.getValue() == null || m.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        TokenService.removeUsers(listForRemove);

    }

}
