package com.uniso.equso.service;

import com.uniso.equso.model.JwtResponse;
import com.uniso.equso.model.LoginRequest;

public interface AuthService {
    JwtResponse createAuthenticationToken(LoginRequest request);

    void logout(String token);
}
