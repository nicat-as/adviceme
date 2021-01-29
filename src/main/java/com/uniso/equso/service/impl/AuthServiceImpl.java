package com.uniso.equso.service.impl;

import com.uniso.equso.config.security.UserDetail;
import com.uniso.equso.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.uniso.equso.config.security.SecurityConstant.TOKEN_PREFIX;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Override
    public void logout(String token) {
        log.info("ActionLog.logout.started");
        var replacedToken = token.replace(TOKEN_PREFIX, "");
        var userId = extractUserId();
        log.debug("Add token to blacklist for user: {}", userId);
        TokenService.addToken(userId, replacedToken);
        log.info("ActionLog.logout.started");
    }

    private Long extractUserId() {
        return ((UserDetail) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUserEntity()
                .getId();
    }
}
