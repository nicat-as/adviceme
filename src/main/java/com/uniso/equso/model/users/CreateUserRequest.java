package com.uniso.equso.model.users;

import com.uniso.equso.annotation.WeakPassword;
import com.uniso.equso.dao.enums.UserSubType;
import com.uniso.equso.dao.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    @Email
    private String email;

    @WeakPassword
    private String password;

    @NotNull
    private UserType type;

    @NotNull
    private UserSubType subType;

    @Size(max = 400)
    private String about;

    private Boolean isAnonymous = false;

}
