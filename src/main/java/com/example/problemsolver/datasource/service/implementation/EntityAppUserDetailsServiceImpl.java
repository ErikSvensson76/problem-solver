package com.example.problemsolver.datasource.service.implementation;

import com.example.problemsolver.datasource.entity.EntityAppUserDetails;
import com.example.problemsolver.datasource.repository.EntityAppUserDetailsRepository;
import com.example.problemsolver.datasource.service.interfaces.EntityAppUserDetailsService;
import com.example.problemsolver.exception.AppResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EntityAppUserDetailsServiceImpl implements EntityAppUserDetailsService {

    private final EntityAppUserDetailsRepository repository;

    @Override
    @Transactional
    public EntityAppUserDetails create(EntityAppUserDetails data) {
        EntityAppUserDetails appUserDetails = new EntityAppUserDetails();
        appUserDetails.setCountry(data.getCountry());
        appUserDetails.setEmail(data.getEmail());
        appUserDetails.setDisplayName(data.getDisplayName());
        appUserDetails.setLocalZoneId(data.getLocalZoneId());

        return repository.save(appUserDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public EntityAppUserDetails findById(String id) {
        return repository.findById(id)
                .orElseThrow(AppResourceNotFoundException.of(id, EntityAppUserDetails.class));
    }

    @Override
    @Transactional
    public EntityAppUserDetails update(EntityAppUserDetails data) {
        EntityAppUserDetails userDetails = findById(data.getId());

        var optional = repository.findByEmail(data.getEmail());
        if(optional.isPresent() && !optional.get().getId().equals(data.getId())){
            throw new IllegalArgumentException("Email: " + data.getEmail() + " is already taken");
        }

        userDetails.setLocalZoneId(data.getLocalZoneId());
        userDetails.setEmail(data.getEmail());
        userDetails.setDisplayName(data.getDisplayName());
        userDetails.setCountry(data.getCountry());

        return repository.save(userDetails);
    }

    @Override
    @Transactional
    public Integer remove(EntityAppUserDetails data) {
        EntityAppUserDetails toRemove = findById(data.getId());
        repository.delete(toRemove);

        if(repository.findById(data.getId()).isPresent()){
            return 0;
        }
        return 1;
    }
}
