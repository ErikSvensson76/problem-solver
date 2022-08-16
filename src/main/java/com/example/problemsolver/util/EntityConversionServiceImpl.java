package com.example.problemsolver.util;

import com.example.problemsolver.datasource.entity.EntityAppUser;
import com.example.problemsolver.datasource.entity.EntityAppUserDetails;
import com.example.problemsolver.model.input.UserCreateInput;
import com.example.problemsolver.model.input.UserDetailsCreateInput;
import com.example.problemsolver.model.response.User;
import com.example.problemsolver.model.response.UserDetails;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class EntityConversionServiceImpl implements EntityConversionService{
    @Override
    public User fromEntityAppUser(EntityAppUser appUser) {
        User user = null;
        if(appUser != null){
            user = new User();
            user.setId(appUser.getId());
            user.setUsername(appUser.getUsername());
            user.setRegistrationDate(
                    OffsetDateTime.of(
                            appUser.getRegistrationDate(), ZoneOffset.ofHours(
                                    appUser.getAppUserDetails().getOffsetHours()
                            )
                    )
            );
            user.setUserDetails(fromEntityAppUserDetails(appUser.getAppUserDetails()));
        }
        return user;
    }

    @Override
    public EntityAppUser fromUserCreateInput(@Valid UserCreateInput input) {
        EntityAppUser entityAppUser = null;
        if(input != null){
            entityAppUser = new EntityAppUser();
            entityAppUser.setUsername(input.getUsername());
            entityAppUser.setPassword(input.getPassword());
            entityAppUser.setAppUserDetails(fromUserDetailsCreateInput(input.getUserDetails()));
        }
        return entityAppUser;
    }

    @Override
    public UserDetails fromEntityAppUserDetails(EntityAppUserDetails appUserDetails) {
        UserDetails userDetails = null;
        if(appUserDetails != null){
            userDetails = new UserDetails();
            userDetails.setEmail(appUserDetails.getEmail());

        }
        return userDetails;
    }

    @Override
    public EntityAppUserDetails fromUserDetailsCreateInput(UserDetailsCreateInput input) {
        EntityAppUserDetails userDetails = null;
        if(input != null){
            userDetails = new EntityAppUserDetails();
            userDetails.setEmail(input.getEmail());
            userDetails.setDisplayName(input.getDisplayName());
            userDetails.setOffsetHours(Integer.parseInt(input.getTimeZone()));

        }
        return userDetails;
    }
}
