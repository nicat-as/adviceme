package com.uniso.equso.controller;

import com.uniso.equso.model.CreateUserRequest;
import com.uniso.equso.model.UserDto;
import com.uniso.equso.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.root}")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("sign-up")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request) {
        userService.addUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
