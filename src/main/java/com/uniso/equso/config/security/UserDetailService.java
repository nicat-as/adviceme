package com.uniso.equso.config.security;

import com.uniso.equso.dao.repository.UserEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    public UserDetailService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("ActionLog.loadUserByUsername.started");
        var user = userEntityRepository.findByEmail(email);
        if (user == null) {
            log.error("exception.user-not-found - email: {}",email);
            throw new UsernameNotFoundException(email);
        }
        log.info("ActionLog.loadUserByUsername.ended");
        return UserDetail.builder()
                .userEntity(user)
                .build();
    }
}
