package com.uniso.equso.controller;

import com.uniso.equso.config.security.CustomUserDetails;
import com.uniso.equso.model.UserDto;
import com.uniso.equso.service.UserService;
import com.uniso.equso.util.AuthenticationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${url.root}")
@Slf4j
public class UserController {
    private final UserService userService;
    private final AuthenticationUtil authenticationUtil;

    public UserController(UserService userService, AuthenticationUtil authenticationUtil) {
        this.userService = userService;
        this.authenticationUtil = authenticationUtil;
    }

    @GetMapping("user/profile")
    public ResponseEntity<UserDto> getUser() {
        var user = (CustomUserDetails) authenticationUtil.getContext().getPrincipal();
        return ResponseEntity.ok(userService.getUserById(user.getUserEntity().getId()));
    }
}
