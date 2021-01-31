package com.uniso.equso.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.uniso.equso.config.security.CustomUserDetails;
import com.uniso.equso.config.security.CustomUserDetailsService;
import com.uniso.equso.dao.entities.UserEntity;
import com.uniso.equso.exceptions.AuthenticationException;
import com.uniso.equso.model.JwtResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.uniso.equso.config.security.SecurityConstant.EXPIRATION_TIME;

@Component
@Slf4j
public class JwtUtil {
    private static final String CLAIM_ID = "id";
    private static final String CLAIM_TYPES = "types";

    private final String secret;
    private final CustomUserDetailsService userDetailsService;

    public JwtUtil(@Value("${jwt.secret}") String secret, CustomUserDetailsService userDetailsService) {
        this.secret = secret;
        this.userDetailsService = userDetailsService;
    }

    public JwtResponse generateToken(UserEntity user) {
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

    public CustomUserDetails validateToken(String token) {
        try {
            var decodedToken = decodedJWT(token);
            return userDetailsService
                    .loadUserByUsername(decodedToken.getSubject());
        } catch (JWTVerificationException e) {
            log.error("exception.");
            throw new AuthenticationException("exception.authentication.invalid-jwt");
        }
    }

    private DecodedJWT decodedJWT(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build()
                .verify(token);
    }

}
