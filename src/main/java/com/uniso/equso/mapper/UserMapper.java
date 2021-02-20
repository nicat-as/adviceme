package com.uniso.equso.mapper;

import com.uniso.equso.config.security.CustomUserDetails;
import com.uniso.equso.dao.entities.UserEntity;
import com.uniso.equso.dao.enums.Status;
import com.uniso.equso.model.users.CreateUserRequest;
import com.uniso.equso.model.users.UpdateUserRequest;
import com.uniso.equso.model.users.UserDto;
import com.uniso.equso.model.users.UserInfo;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class UserMapper {
    public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "type", target = "userType")
    public abstract UserInfo entityToUserInfo(UserEntity entity);

    @Mapping(target = "alias", expression = "java(buildAlias(request.getName(),request.getSurname()))")
    @Mapping(target = "status", expression = "java(getDefaultStatus())")
    public abstract UserEntity createUserToEntity(CreateUserRequest request);

    public abstract UserDto entityToUserDto(UserEntity entity);

    @BeforeMapping
    protected void setUserInfoAnonymous(UserEntity userEntity, @MappingTarget UserInfo userInfo) {
        makeAnonymous(userEntity);
    }

    @BeforeMapping
    protected void setUserDtoAnonymous(UserEntity userEntity, @MappingTarget UserDto userDto) {
        makeAnonymous(userEntity);
    }

    protected void makeAnonymous(UserEntity userEntity) {
        Long id = null;
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ( principal instanceof CustomUserDetails){
            id = ((CustomUserDetails)principal).getUserEntity().getId();
        }

        if (userEntity.getIsAnonymous() && !Objects.equals(id, userEntity.getId())) {
            userEntity.setName(userEntity.getAlias());
            userEntity.setSurname(null);
            userEntity.setEmail(null);
            userEntity.setAbout(null);
        }
    }

    protected String buildAlias(String name, String surname) {
        return String.valueOf(new char[]{
                name.toUpperCase().charAt(0),
                surname.toUpperCase().charAt(0)
        });
    }

    protected Status getDefaultStatus() {
        return Status.ACTIVE;
    }


    public abstract UserEntity updateEntity(UpdateUserRequest updateUser,
                                            @MappingTarget UserEntity entity);
}
