package com.uniso.equso.util;

import com.uniso.equso.config.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtil {

    public Authentication getContext() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public CustomUserDetails getUserDetail() {
        return (CustomUserDetails) getContext().getPrincipal();
    }

    public Long getUserId(){
        return getUserDetail().getUserEntity().getId();
    }
}
