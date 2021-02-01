package com.uniso.equso.controller;

import com.uniso.equso.model.CheckEmailResponse;
import com.uniso.equso.model.UserDto;
import com.uniso.equso.service.UserService;
import com.uniso.equso.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;

@RestController
@RequestMapping("${url.root}/user")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;
    private final AuthenticationUtil authenticationUtil;


    @GetMapping("profile")
    public ResponseEntity<UserDto> getUserProfile() {
        var user = authenticationUtil.getUserDetail();
        return ResponseEntity.ok(userService.getUserById(user.getUserEntity().getId()));
    }

    @GetMapping("email")
    public ResponseEntity<CheckEmailResponse> checkEmail(@RequestParam("q") @Email String query) {
        return ResponseEntity.ok(userService.isValidEmail(query));
    }


}
