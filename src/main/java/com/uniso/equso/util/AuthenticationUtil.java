package com.uniso.equso.util;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtil {

    public Authentication getContext(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
