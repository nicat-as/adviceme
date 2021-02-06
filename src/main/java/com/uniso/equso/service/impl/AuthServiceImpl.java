package com.uniso.equso.service.impl;

import com.uniso.equso.config.security.CustomUserDetails;
import com.uniso.equso.config.security.CustomUserDetailsService;
import com.uniso.equso.exceptions.AuthenticationException;
import com.uniso.equso.model.auth.JwtResponse;
import com.uniso.equso.model.auth.LoginRequest;
import com.uniso.equso.service.AuthService;
import com.uniso.equso.service.TokenService;
import com.uniso.equso.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.uniso.equso.config.security.SecurityConstant.TOKEN_PREFIX;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           CustomUserDetailsService customUserDetailsService,

                           JwtUtil jwtUtil, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
    }

    @Override
    public JwtResponse createAuthenticationToken(LoginRequest request) {
        log.info("ActionLog.authenticate.started");
        JwtResponse response;
        try {
            authenticate(request.getEmail(), request.getPassword());
            var userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
            response = jwtUtil.generateToken(userDetails.getUserEntity());
        } catch (UsernameNotFoundException e) {
            log.error("exception.username-not-found", e);
            throw new AuthenticationException("exception.authentication.username-not-found");
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
        tokenService.addToken(userId, replacedToken);
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


}
