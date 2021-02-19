package com.uniso.equso.service;

import com.uniso.equso.model.auth.CheckEmailResponse;
import com.uniso.equso.model.users.CreateUserRequest;
import com.uniso.equso.model.users.UpdateUserRequest;
import com.uniso.equso.model.users.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(CreateUserRequest userRequest);

    UserDto getUserById(Long userId);

    CheckEmailResponse isValidEmail(String email);

    List<UserDto> getSpecialistUsers(Integer page, Integer size);

    UserDto updateProfile(UpdateUserRequest request);
}
