package com.example.problemsolver.datasource.service.implementation;

import com.example.problemsolver.datasource.entity.EntityAppRole;
import com.example.problemsolver.datasource.entity.EntityAppUser;
import com.example.problemsolver.datasource.entity.UserRole;
import com.example.problemsolver.datasource.repository.EntityAppRoleRepository;
import com.example.problemsolver.datasource.repository.EntityAppUserRepository;
import com.example.problemsolver.datasource.service.interfaces.EntityAppRoleService;
import com.example.problemsolver.exception.AppResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EntityAppRoleServiceImpl implements EntityAppRoleService {

    private final EntityAppRoleRepository repository;
    private final EntityAppUserRepository appUserRepository;

    @Override
    @Transactional
    public EntityAppRole create(EntityAppRole data) {
        EntityAppRole entityAppRole = new EntityAppRole();
        entityAppRole.setUserRole(data.getUserRole());
        return repository.save(entityAppRole);
    }

    @Override
    @Transactional(readOnly = true)
    public EntityAppRole findById(String id) {
        return repository.findById(id)
                .orElseThrow(AppResourceNotFoundException.of(id, EntityAppRole.class));
    }

    @Override
    @Transactional
    public EntityAppRole update(EntityAppRole data) {
        EntityAppRole entityAppRole = findById(data.getId());
        entityAppRole.setUserRole(data.getUserRole());
        return repository.save(entityAppRole);
    }

    @Override
    @Transactional
    public Integer remove(EntityAppRole data) {
        EntityAppRole entityAppRole = findById(data.getId());
        entityAppRole.setAppUsers(null);

        repository.delete(entityAppRole);

        if(repository.findById(data.getId()).isPresent()){
            return 0;
        }
        return 1;
    }

    @Override
    @Transactional
    public EntityAppRole addAEntityAppUser(UserRole role, String appUserId){
        EntityAppRole entityAppRole = repository.findByUserRole(role)
                .orElseThrow(() -> new AppResourceNotFoundException("Couldn't find EntityAppRole with UserRole " + role));

        EntityAppUser entityAppUser = appUserRepository.findById(appUserId)
                .orElseThrow(AppResourceNotFoundException.of(appUserId, EntityAppUser.class));

        entityAppRole.getAppUsers().add(entityAppUser);
        entityAppUser.getAppRoles().add(entityAppRole);

        return repository.save(entityAppRole);
    }

    @Override
    @Transactional
    public EntityAppRole removeEntityAppUser(UserRole role, String appUserId){
        EntityAppRole entityAppRole = repository.findByUserRole(role)
                .orElseThrow(() -> new AppResourceNotFoundException("Couldn't find EntityAppRole with UserRole " + role));

        EntityAppUser entityAppUser = appUserRepository.findById(appUserId)
                .orElseThrow(AppResourceNotFoundException.of(appUserId, EntityAppUser.class));

        entityAppRole.getAppUsers().remove(entityAppUser);
        entityAppUser.getAppRoles().remove(entityAppRole);

        return repository.save(entityAppRole);
    }
}
