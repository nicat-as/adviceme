package com.uniso.equso.model.users;

import com.uniso.equso.dao.enums.UserSubType;
import com.uniso.equso.dao.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    private String name;
    private String surname;
    private Boolean isAnonymous;
    private String about;
    private UserType type;
    private UserSubType subType;
}
