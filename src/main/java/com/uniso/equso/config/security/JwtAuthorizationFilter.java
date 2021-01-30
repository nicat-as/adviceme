package com.uniso.equso.config.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.uniso.equso.service.TokenService;
import com.uniso.equso.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.uniso.equso.config.security.SecurityConstant.HEADER_STRING;
import static com.uniso.equso.config.security.SecurityConstant.TOKEN_PREFIX;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, TokenService tokenService) {
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        log.info("ActionLog.doFilterInternal.start");
        String header = request.getHeader(HEADER_STRING);

        log.debug("Check token null or not");
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        log.debug("Prepare auth");
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        log.debug("Set context");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("ActionLog.doFilterInternal.ended");
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        log.info("ActionLog.getAuthentication.started");

        String token = request.getHeader(HEADER_STRING);
        UsernamePasswordAuthenticationToken authenticationToken = null;
        try {
            if (token != null) {
                var replacedToken = token.replace(TOKEN_PREFIX, "");
                var user = jwtUtil.validateToken(replacedToken);
                if (user != null && tokenService.validateToken(user.getUserEntity().getId(), replacedToken)) {
                    authenticationToken = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                }
            }
        } catch (JWTVerificationException e) {
            log.error("exception.jwt-verification-failed", e);
            throw e;
        }
        log.info("ActionLog.getAuthentication.ended");
        return authenticationToken;
    }
}
