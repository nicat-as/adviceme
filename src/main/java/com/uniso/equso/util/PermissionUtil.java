package com.uniso.equso.util;

import com.uniso.equso.dao.enums.UserSubType;
import com.uniso.equso.dao.enums.UserType;
import com.uniso.equso.exceptions.AuthorizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.uniso.equso.dao.enums.UserSubType.*;
import static com.uniso.equso.dao.enums.UserType.SPECIALIST;
import static com.uniso.equso.dao.enums.UserType.USER;


@Component("permissionUtil")
@Slf4j
public class PermissionUtil {

    private Map<UserType, List<UserSubType>> typePolicy;

    @PostConstruct
    public void init() {
        typePolicy = new HashMap<>();
        typePolicy.put(USER, List.of(DEFAULT));
        typePolicy.put(SPECIALIST, List.of(
                PSYCHOLOGIST,
                DOCTOR,
                LAWYER
        ));
    }

    public boolean isSubType(UserType type, UserSubType subType) {
        log.info("ActionLog.isSubType.start - {} {}", type, subType);
        if (!typePolicy.get(type).contains(subType)) {
            log.error("ActionLog.isSubType.error - not permitted for selecting type: {} {}",type,subType);
            throw new AuthorizationException("exception.authorization.wrong-user-type",
                    "You're not permitted for selecting this type");
        }
        log.info("ActionLog.isSubType.end - {} {}", type, subType);
        return true;
    }

}
