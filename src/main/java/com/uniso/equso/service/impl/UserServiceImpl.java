package com.uniso.equso.service.impl;

import com.uniso.equso.dao.entities.UserEntity;
import com.uniso.equso.dao.enums.Status;
import com.uniso.equso.dao.enums.UserSubType;
import com.uniso.equso.dao.enums.UserType;
import com.uniso.equso.dao.repository.UserEntityRepository;
import com.uniso.equso.model.CreateUserRequest;
import com.uniso.equso.model.UserDto;
import com.uniso.equso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addUser(CreateUserRequest userRequest) {
        log.info("ActionLog.addUser.started - user:{}", userRequest.getEmail());
        var userEntity = UserEntity.builder()
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .email(userRequest.getEmail())
                .alias(UUID.randomUUID().toString())
                .isAnonymous(false)
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .type(UserType.USER)
                .subType(UserSubType.DEFAULT)
                .status(Status.ACTIVE)
                .build();

        log.debug("save new user");
        userEntityRepository.save(userEntity);
        log.info("ActionLog.addUser.ended");
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("ActionLog.getUserById.started - user:{}", userId);

        var user = userEntityRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new RuntimeException("exception.user-not-found");
                });

        log.info("ActionLog.getUserById.ended - user:{}", userId);

        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .type(user.getType())
                .subType(user.getSubType())
                .build();
    }
}
