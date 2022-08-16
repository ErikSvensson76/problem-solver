package com.example.problemsolver.util;

import com.example.problemsolver.datasource.entity.EntityAppUser;
import com.example.problemsolver.datasource.entity.EntityAppUserDetails;
import com.example.problemsolver.model.input.UserCreateInput;
import com.example.problemsolver.model.input.UserDetailsCreateInput;
import com.example.problemsolver.model.response.User;
import com.example.problemsolver.model.response.UserDetails;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public interface EntityConversionService {

    default OffsetDateTime toOffsetDateTime(LocalDateTime dateTime, Integer offSetHours){
        return OffsetDateTime.of(dateTime, ZoneOffset.ofHours(offSetHours));
    }

    User fromEntityAppUser(EntityAppUser appUser);
    EntityAppUser fromUserCreateInput(UserCreateInput input);

    UserDetails fromEntityAppUserDetails(EntityAppUserDetails appUserDetails);
    EntityAppUserDetails fromUserDetailsCreateInput(UserDetailsCreateInput input);
}
