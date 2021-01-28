package com.uniso.equso.dao.model;

import com.uniso.equso.dao.enums.UserSubType;
import com.uniso.equso.dao.enums.UserType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
@Data
public class UserEntity extends BaseEntity {
    @Enumerated(value = EnumType.STRING)
    private UserType type;

    @Enumerated(value = EnumType.STRING)
    private UserSubType subType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String alias;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isAnonymous;
}
