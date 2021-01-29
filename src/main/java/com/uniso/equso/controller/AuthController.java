package com.uniso.equso.controller;

import com.uniso.equso.model.LoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("login")
    public void fakeLogin(@RequestBody LoginRequest request) {

    }

    @PostMapping("logout")
    public void fakeLogout() {

    }
}
