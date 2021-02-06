package com.uniso.equso.model.users;

import com.uniso.equso.annotation.StrongPassword;
import com.uniso.equso.dao.enums.UserSubType;
import com.uniso.equso.dao.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Constraint;
import javax.validation.constraints.*;

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

    @StrongPassword
    private String password;

    @NotNull
    private UserType type;

    @NotNull
    private UserSubType subType;

    @Size(max = 400)
    private String about;

    private Boolean isAnonymous = false;

}
