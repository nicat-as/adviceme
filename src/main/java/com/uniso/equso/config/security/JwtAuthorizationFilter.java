package com.uniso.equso.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.uniso.equso.dao.entities.UserEntity;
import com.uniso.equso.service.impl.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private final String secret;

    public JwtAuthorizationFilter(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
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
        if (token != null) {
            try {
                var decodedJWT = JWT.require(Algorithm.HMAC512(secret.getBytes()))
                        .build()
                        .verify(token.replace(TOKEN_PREFIX, ""));
                var user = CustomUserDetails.builder()
                        .userEntity(UserEntity.builder()
                                .id(decodedJWT.getClaim("id").asLong())
                                .email(decodedJWT.getSubject())
                                .build())
                        .build();

                if (user != null && TokenService.validateToken(user.getUserEntity().getId(), decodedJWT.getToken())) {
                    authenticationToken = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                }
            } catch (JWTVerificationException e) {
                log.error("exception.jwt-verification-failed", e);
                throw e;
            }
        }
        log.info("ActionLog.getAuthentication.started");
        return authenticationToken;
    }
}
