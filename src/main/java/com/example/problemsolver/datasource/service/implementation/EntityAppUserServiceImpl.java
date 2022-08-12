package com.example.problemsolver.datasource.service.implementation;

import com.example.problemsolver.datasource.entity.EntityAppRole;
import com.example.problemsolver.datasource.entity.EntityAppUser;
import com.example.problemsolver.datasource.entity.UserRole;
import com.example.problemsolver.datasource.repository.EntityAppRoleRepository;
import com.example.problemsolver.datasource.repository.EntityAppUserRepository;
import com.example.problemsolver.datasource.service.interfaces.EntityAppUserDetailsService;
import com.example.problemsolver.datasource.service.interfaces.EntityAppUserService;
import com.example.problemsolver.exception.AppResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;


@Service
@RequiredArgsConstructor
public class EntityAppUserServiceImpl implements EntityAppUserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final EntityAppRoleRepository appRoleRepository;
    private final EntityAppUserDetailsService userDetailsService;
    private final EntityAppUserRepository repository;

    @Override
    @Transactional
    public EntityAppUser create(EntityAppUser data) {
        return internalCreate(data, UserRole.APP_USER);
    }

    public EntityAppRole fetchAppRole(UserRole role){
        return appRoleRepository.findByUserRole(role)
                .orElseThrow(() -> new AppResourceNotFoundException("Couldn't find EntityAppRole: " + role));
    }

    public EntityAppUser internalCreate(EntityAppUser data, UserRole role){
        EntityAppUser entityAppUser = new EntityAppUser();
        entityAppUser.setActive(true);
        entityAppUser.setUsername(data.getUsername().trim());
        entityAppUser.setPassword(passwordEncoder.encode(data.getPassword()));
        entityAppUser.setAppUserDetails(userDetailsService.create(data.getAppUserDetails()));

        repository.save(entityAppUser);

        var appRole = fetchAppRole(role);
        entityAppUser.setAppRoles(new HashSet<>(Collections.singletonList(appRole)));
        return repository.save(entityAppUser);
    }


    @Override
    @Transactional(readOnly = true)
    public EntityAppUser findById(String id) {
        return repository.findById(id)
                .orElseThrow(AppResourceNotFoundException.of(id, EntityAppUser.class));
    }

    @Override
    @Transactional
    public EntityAppUser update(EntityAppUser data) {
        var toUpdate = findById(data.getId());

        var optional = repository.findByUsername(data.getUsername());
        if(optional.isPresent() && !optional.get().getId().equals(toUpdate.getId())){
            throw new IllegalArgumentException("Username " + data.getUsername() + " is already taken");
        }
        toUpdate.setUsername(data.getUsername());
        toUpdate.setAppUserDetails(
                userDetailsService.update(toUpdate.getAppUserDetails())
        );


        return repository.save(toUpdate);
    }

    @Override
    @Transactional
    public Integer remove(EntityAppUser data) {
        var toRemove = findById(data.getId());

        Integer count = userDetailsService.remove(data.getAppUserDetails());
        toRemove.setAppUserDetails(null);

        toRemove.setAppRoles(null);

        repository.delete(toRemove);
        if(repository.findById(data.getId()).isPresent()){
            return count;
        }
        return count + 1;
    }

    @Override
    @Transactional
    public EntityAppUser createAdmin(EntityAppUser data) {
        return internalCreate(data, UserRole.APP_ADMIN);
    }
}
