package com.example.problemsolver.config;

import com.example.problemsolver.datasource.entity.EntityAppRole;
import com.example.problemsolver.datasource.entity.UserRole;
import com.example.problemsolver.datasource.repository.EntityAppRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class EntitySetupConfig {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final EntityAppRoleRepository entityAppRoleRepository;

    @PostConstruct
    @Transactional
    public void postConstruct(){
        if(!activeProfile.equals("test")){
            if(entityAppRoleRepository.count() == 0){
                Arrays.stream(UserRole.values()).forEach(role -> entityAppRoleRepository.save(new EntityAppRole(null, role, null)));
            }
        }

    }


}
