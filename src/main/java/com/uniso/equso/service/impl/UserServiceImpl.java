package com.uniso.equso.service.impl;

import com.uniso.equso.dao.entities.UserEntity;
import com.uniso.equso.dao.enums.Status;
import com.uniso.equso.dao.repository.UserEntityRepository;
import com.uniso.equso.exceptions.AuthenticationException;
import com.uniso.equso.exceptions.UserException;
import com.uniso.equso.model.auth.CheckEmailResponse;
import com.uniso.equso.model.users.CreateUserRequest;
import com.uniso.equso.model.users.UserDto;
import com.uniso.equso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        if (!isValidEmail(userRequest.getEmail()).getIsValid()) {
            throw new AuthenticationException("exception.email-is-not-valid");
        }

        var userEntity = UserEntity.builder()
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .email(userRequest.getEmail())
                .alias(buildAlias(userRequest.getName(), userRequest.getSurname()))
                .isAnonymous(userRequest.getIsAnonymous())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .type(userRequest.getType())
                .subType(userRequest.getSubType())
                .about(userRequest.getAbout())
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
                    throw new UserException("exception.user-not-found");
                });

        log.info("ActionLog.getUserById.ended - user:{}", userId);

        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .type(user.getType())
                .subType(user.getSubType())
                .about(user.getAbout())
                .build();
    }

    @Override
    public CheckEmailResponse isValidEmail(String email) {
        var isExist = userEntityRepository.existsByEmail(email);
        return CheckEmailResponse.builder()
                .isValid(!isExist)
                .build();
    }

    private String buildAlias(String name, String surname) {
        return String.valueOf(new char[]{
                name.toUpperCase().charAt(0),
                surname.toUpperCase().charAt(0)
        });
    }
}
