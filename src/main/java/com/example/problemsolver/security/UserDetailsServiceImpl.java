package com.example.problemsolver.security;

import com.example.problemsolver.datasource.entity.EntityAppUser;
import com.example.problemsolver.datasource.repository.EntityAppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EntityAppUserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public SecurityUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EntityAppUser appUser = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user was found"));

        SecurityUserDetails securityUserDetails = new SecurityUserDetails();
        securityUserDetails.setId(appUser.getId());
        securityUserDetails.setUsername(appUser.getUsername());
        securityUserDetails.setPassword(appUser.getPassword());
        securityUserDetails.setActive(appUser.isActive());
        securityUserDetails.setUserDetailsId(appUser.getAppUserDetails().getId());
        securityUserDetails.setEmail(appUser.getAppUserDetails().getEmail());
        return securityUserDetails;
    }
}
