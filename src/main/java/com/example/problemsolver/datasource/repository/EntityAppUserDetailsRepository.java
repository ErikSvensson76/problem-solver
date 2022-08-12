package com.example.problemsolver.datasource.repository;

import com.example.problemsolver.datasource.entity.EntityAppUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EntityAppUserDetailsRepository extends JpaRepository<EntityAppUserDetails, String> {

    @Query("SELECT ud FROM EntityAppUserDetails ud WHERE UPPER(ud.email) = UPPER(:email)")
    Optional<EntityAppUserDetails> findByEmail(@Param("email") String email);

}