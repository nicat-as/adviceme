package com.uniso.equso.service.impl;

import com.uniso.equso.dao.repository.UserEntityRepository;
import com.uniso.equso.exceptions.AuthenticationException;
import com.uniso.equso.exceptions.UserException;
import com.uniso.equso.mapper.UserMapper;
import com.uniso.equso.model.auth.CheckEmailResponse;
import com.uniso.equso.model.users.CreateUserRequest;
import com.uniso.equso.model.users.UserDto;
import com.uniso.equso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserEntityRepository userEntityRepository;

    public UserServiceImpl(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @PreAuthorize("@permissionUtil.isSubType(#userRequest.getType(),#userRequest.getSubType())")
    @Override
    public UserDto addUser(CreateUserRequest userRequest) {
        log.info("ActionLog.addUser.started - user:{}", userRequest.getEmail());

        if (!isValidEmail(userRequest.getEmail()).getIsValid()) {
            throw new AuthenticationException("exception.email-is-not-valid");
        }

        userRequest.setPassword(new BCryptPasswordEncoder().encode(userRequest.getPassword()));

        var userEntity = UserMapper.INSTANCE.createUserToEntity(userRequest);

        log.debug("save new user:{}",userEntity);

        userEntityRepository.save(userEntity);

        log.info("ActionLog.addUser.ended");

        return UserMapper.INSTANCE.entityToUserDto(userEntity);
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("ActionLog.getUserById.started - user:{}", userId);

        var user = userEntityRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new UserException("exception.user-not-found");
                });

        log.info("ActionLog.getUserById.ended - user:{}", userId);

        return UserMapper.INSTANCE.entityToUserDto(user);
    }

    @Override
    public CheckEmailResponse isValidEmail(String email) {
        var isExist = userEntityRepository.existsByEmail(email);
        return CheckEmailResponse.builder()
                .isValid(!isExist)
                .build();
    }

}
