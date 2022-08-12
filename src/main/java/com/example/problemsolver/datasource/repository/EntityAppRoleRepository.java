package com.example.problemsolver.datasource.repository;

import com.example.problemsolver.datasource.entity.EntityAppRole;
import com.example.problemsolver.datasource.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface EntityAppRoleRepository extends JpaRepository<EntityAppRole, String> {

    Optional<EntityAppRole> findByUserRole(UserRole userRole);

}