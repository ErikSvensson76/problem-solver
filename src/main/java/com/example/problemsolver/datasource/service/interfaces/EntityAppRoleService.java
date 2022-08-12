package com.example.problemsolver.datasource.service.interfaces;

import com.example.problemsolver.datasource.entity.EntityAppRole;
import com.example.problemsolver.datasource.entity.UserRole;
import org.springframework.transaction.annotation.Transactional;

public interface EntityAppRoleService extends GenericMutationCrud<EntityAppRole, String>{

    @Transactional
    EntityAppRole addAEntityAppUser(UserRole role, String appUserId);

    @Transactional
    EntityAppRole removeEntityAppUser(UserRole role, String appUserId);
}
