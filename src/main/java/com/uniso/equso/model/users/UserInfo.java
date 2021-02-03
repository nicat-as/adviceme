package com.uniso.equso.model.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uniso.equso.dao.enums.UserSubType;
import com.uniso.equso.dao.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private UserType userType;
    private UserSubType subType;
}
