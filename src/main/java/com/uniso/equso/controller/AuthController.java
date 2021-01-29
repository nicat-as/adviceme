package com.uniso.equso.controller;

import com.uniso.equso.config.security.SecurityConstant;
import com.uniso.equso.model.LoginRequest;
import com.uniso.equso.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public void login(@RequestBody LoginRequest request) {

    }

    @PostMapping("logout")
    public void logout(HttpServletRequest request) {
        log.info("ActionLog.logout.started");
        var token = Optional.of(request.getHeader(SecurityConstant.HEADER_STRING))
                .orElseThrow(()->{throw new RuntimeException("exception.header-not-found");});
        authService.logout(token);
        log.info("ActionLog.logout.ended");
    }
}
