package com.uniso.equso.config.security;


import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniso.equso.model.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.uniso.equso.config.security.SecurityConstant.EXPIRATION_TIME;
import static com.uniso.equso.config.security.SecurityConstant.TOKEN;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private static final String AUTH_EXCEPTION = "exception.failed-authentication";


    private final AuthenticationManager authenticationManager;
    private final String secret;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, String secret) {
        this.authenticationManager = authenticationManager;
        this.secret = secret;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        log.info("ActionLog.attemptAuthentication.start");
        try {
            var loginRequest = new ObjectMapper()
                    .readValue(req.getInputStream(), LoginRequest.class);

            log.debug("Attempt login for user: {}", loginRequest.getEmail());

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            log.error("Exception.attemptAuthentication.failed-authentication");
            throw new RuntimeException(AUTH_EXCEPTION, e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        log.info("ActionLog.successfulAuthentication.start");

        var user = ((UserDetail) authResult.getPrincipal());
        var issuedDate = new Date(System.currentTimeMillis());
        var expiredDate = new Date(issuedDate.getTime() + EXPIRATION_TIME);

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("id", user.getUserEntity().getId())
                .withClaim("types", List.of(user.getUserEntity().getType().name(),
                        user.getUserEntity().getSubType().name())
                )
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(issuedDate)
                .withExpiresAt(expiredDate)
                .sign(HMAC512(secret.getBytes()));


        addResponseBody(response, token);

        log.info("ActionLog.successfulAuthentication.ended");
    }

    private void addResponseBody(HttpServletResponse response, String token) {
        log.info("ActionLog.addResponseBody.started");
        try {
            var mapper = new ObjectMapper();
            var result = mapper.writeValueAsString(Map.of(TOKEN, token));
            response.getOutputStream().println(result);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        } catch (IOException e) {
            log.error("Exception.io:", e);
        }
        log.info("ActionLog.addResponseBody.ended");
    }
}
