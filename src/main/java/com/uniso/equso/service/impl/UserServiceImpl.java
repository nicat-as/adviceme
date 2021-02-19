package com.uniso.equso.service.impl;

import com.uniso.equso.dao.enums.Status;
import com.uniso.equso.dao.enums.UserType;
import com.uniso.equso.dao.repository.UserEntityRepository;
import com.uniso.equso.exceptions.AuthenticationException;
import com.uniso.equso.exceptions.UserException;
import com.uniso.equso.mapper.UserMapper;
import com.uniso.equso.model.auth.CheckEmailResponse;
import com.uniso.equso.model.users.CreateUserRequest;
import com.uniso.equso.model.users.UpdateUserRequest;
import com.uniso.equso.model.users.UserDto;
import com.uniso.equso.service.UserService;
import com.uniso.equso.util.AuthenticationUtil;
import com.uniso.equso.util.PermissionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserEntityRepository userEntityRepository;
    private final AuthenticationUtil authenticationUtil;
    private final PermissionUtil permissionUtil;


    @PreAuthorize("@permissionUtil.isSubType(#userRequest.getType(),#userRequest.getSubType())")
    @Override
    public UserDto addUser(CreateUserRequest userRequest) {
        log.info("ActionLog.addUser.started - user:{}", userRequest.getEmail());

        if (!isValidEmail(userRequest.getEmail()).getIsValid()) {
            throw new AuthenticationException("exception.email-is-not-valid");
        }

        userRequest.setPassword(new BCryptPasswordEncoder().encode(userRequest.getPassword()));

        var userEntity = UserMapper.INSTANCE.createUserToEntity(userRequest);

        log.debug("save new user:{}", userEntity);

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
        log.info("ActionLog.isValidEmail.start - email:{}", email);

        var isExist = userEntityRepository.existsByEmail(email);

        log.debug("Email:{} is exist: {}", email, isExist);

        log.info("ActionLog.isValidEmail.end - email:{}", email);
        return CheckEmailResponse.builder()
                .isValid(!isExist)
                .build();
    }

    @Override
    public List<UserDto> getSpecialistUsers(Integer page, Integer size) {
        log.info("ActionLog.getSpecialistUsers.start");

        var specialistUsers = userEntityRepository
                .findAllByTypeAndStatus(UserType.SPECIALIST, Status.ACTIVE,
                        PageRequest.of(page - 1, size));

        log.debug("Page size: {}", specialistUsers.getTotalElements());

        log.info("ActionLog.getSpecialistUsers.end");

        return specialistUsers.stream()
                .map(UserMapper.INSTANCE::entityToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateProfile(UpdateUserRequest request) {
        log.info("ActionLog.updateProfile.start - userId:{}", authenticationUtil.getUserId());

        var user = userEntityRepository.findByIdAndStatus(authenticationUtil.getUserId(), Status.ACTIVE)
                .orElseThrow(() -> new UserException("exception.user-not-found"));

        log.debug("Update request: {}", request);

        var type = request.getType() == null ? user.getType() : request.getType();
        var subType = request.getSubType() == null ? user.getSubType() : request.getSubType();
        permissionUtil.isSubType(type, subType);

        user = UserMapper.INSTANCE.updateEntity(request, user);

        log.info("ActionLog.updateProfile.end - userId:{}", authenticationUtil.getUserId());
        return UserMapper.INSTANCE.entityToUserDto(userEntityRepository.save(user));

    }

}
