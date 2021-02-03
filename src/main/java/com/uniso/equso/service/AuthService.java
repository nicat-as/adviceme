package com.uniso.equso.service;

import com.uniso.equso.model.auth.JwtResponse;
import com.uniso.equso.model.auth.LoginRequest;

public interface AuthService {
    JwtResponse createAuthenticationToken(LoginRequest request);

    void logout(String token);
}
