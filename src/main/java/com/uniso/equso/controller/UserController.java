package com.uniso.equso.controller;

import com.uniso.equso.model.Wrapper;
import com.uniso.equso.model.auth.CheckEmailResponse;
import com.uniso.equso.model.users.UpdateUserRequest;
import com.uniso.equso.model.users.UserDto;
import com.uniso.equso.service.UserService;
import com.uniso.equso.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.util.List;

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

    @GetMapping("specialist")
    public ResponseEntity<Wrapper<List<UserDto>>> getSpecialistUsers(
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        return ResponseEntity.ok(new Wrapper(userService.getSpecialistUsers(page, size)));
    }

    @PutMapping("profile")
    public ResponseEntity<UserDto> updateProfile(
            @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(userService.updateProfile(request));
    }

}
