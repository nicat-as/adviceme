package com.uniso.equso.service;

import com.uniso.equso.model.CheckEmailResponse;
import com.uniso.equso.model.CreateUserRequest;
import com.uniso.equso.model.UserDto;

public interface UserService {
    void addUser(CreateUserRequest userRequest);

    UserDto getUserById(Long userId);

    CheckEmailResponse isValidEmail(String email);
}
