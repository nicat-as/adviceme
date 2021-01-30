package com.uniso.equso.config.security;

import com.uniso.equso.dao.enums.Status;
import com.uniso.equso.dao.entities.UserEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
public class CustomUserDetails implements UserDetails {

    private UserEntity userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(userEntity.getType().name()),
                new SimpleGrantedAuthority(userEntity.getSubType().name())
        );
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !userEntity.getStatus().equals(Status.EXPIRED);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !userEntity.getStatus().equals(Status.BLOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !userEntity.getStatus().equals(Status.DORMANT);
    }

    @Override
    public boolean isEnabled() {
        return userEntity.getStatus().equals(Status.ACTIVE);
    }
}
