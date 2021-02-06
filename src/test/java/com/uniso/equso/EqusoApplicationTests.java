package com.uniso.equso;

import com.uniso.equso.dao.entities.UserEntity;
import com.uniso.equso.dao.enums.Status;
import com.uniso.equso.dao.enums.UserSubType;
import com.uniso.equso.dao.enums.UserType;
import com.uniso.equso.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;


class EqusoApplicationTests {

    @Test
    void contextLoads() {
        var user = UserEntity.builder()
                .id(1L)
                .name("Nicat")
                .surname("Asgerzade")
                .email("nicat@gmail.com")
                .about("asdrb")
                .password("asdevwv")
                .isAnonymous(true)
                .status(Status.ACTIVE)
                .alias("NA")
                .createdAt(LocalDateTime.now())
                .lastUpdatedAt(LocalDateTime.now())
                .type(UserType.USER)
                .subType(UserSubType.DEFAULT)
                .build();
        var result = UserMapper.INSTANCE.entityToUserInfo(user);
        System.out.println(result);
    }

}
