package com.example.problemsolver.datasource.repository;

import com.example.problemsolver.datasource.entity.EntityAppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntityAppUserRepository extends JpaRepository<EntityAppUser, String> {

    @Query("SELECT u FROM EntityAppUser u WHERE UPPER(u.username) = UPPER(:username)")
    Optional<EntityAppUser> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM EntityAppUser u WHERE u.suspendedUntil = :suspendedStatus")
    List<EntityAppUser> findBySuspended(@Param("suspendedStatus") boolean suspendedStatus);

}