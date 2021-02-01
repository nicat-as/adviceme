package com.uniso.equso.model;

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
public class UserInfo {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private UserType userType;
    private UserSubType subType;
}
