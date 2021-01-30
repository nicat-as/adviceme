package com.uniso.equso.service.impl;

import com.auth0.jwt.JWT;
import com.uniso.equso.config.security.CustomUserDetails;
import com.uniso.equso.config.security.CustomUserDetailsService;
import com.uniso.equso.dao.entities.UserEntity;
import com.uniso.equso.exceptions.AuthenticationException;
import com.uniso.equso.model.JwtResponse;
import com.uniso.equso.model.LoginRequest;
import com.uniso.equso.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.uniso.equso.config.security.SecurityConstant.EXPIRATION_TIME;
import static com.uniso.equso.config.security.SecurityConstant.TOKEN_PREFIX;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private static final String CLAIM_ID = "id";
    private static final String CLAIM_TYPES = "types";

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;


    private final String secret;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           CustomUserDetailsService customUserDetailsService,
                           @Value("${jwt.secret}") String secret
    ) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.secret = secret;
    }

    @Override
    public JwtResponse createAuthenticationToken(LoginRequest request) {
        log.info("ActionLog.authenticate.started");
        JwtResponse response;
        try {
            authenticate(request.getEmail(), request.getPassword());
            var userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
            response = generateToken(userDetails.getUserEntity());
        } catch (UsernameNotFoundException e) {
            log.error("exception.username-not-found", e);
            throw new AuthenticationException("exception.username-not-found");
        }
        log.info("ActionLog.authenticate.ended");
        return response;
    }

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
        return ((CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUserEntity()
                .getId();
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password, null));
        } catch (DisabledException e) {
            log.error("exception.authentication.user-disabled", e);
            throw new AuthenticationException("exception.authentication.user-disabled");
        } catch (LockedException e) {
            log.error("exception.authentication.user-locked", e);
            throw new AuthenticationException("exception.authentication.user-locked");
        } catch (BadCredentialsException e) {
            log.error("exception.authentication.user-bad-credentials", e);
            throw new AuthenticationException("exception.authentication.user-bad-credentials");
        } catch (Exception e) {
            log.error("exception.unknown", e);
            throw e;
        }
    }

    private JwtResponse generateToken(UserEntity user) {

        var issuedDate = new Date(System.currentTimeMillis());
        var expiredDate = new Date(issuedDate.getTime() + EXPIRATION_TIME);

        String token = JWT.create()
                .withSubject(user.getEmail())
                .withClaim(CLAIM_ID, user.getId())
                .withClaim(CLAIM_TYPES, List.of(
                        user.getType().name(),
                        user.getSubType().name()))
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(issuedDate)
                .withExpiresAt(expiredDate)
                .sign(HMAC512(secret.getBytes()));

        return JwtResponse.builder()
                .accessToken(token)
                .build();
    }


}
