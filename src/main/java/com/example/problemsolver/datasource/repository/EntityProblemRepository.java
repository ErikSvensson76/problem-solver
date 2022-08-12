package com.example.problemsolver.datasource.repository;

import com.example.problemsolver.datasource.entity.EntityProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityProblemRepository extends JpaRepository<EntityProblem, String> {
    @Query("SELECT p FROM EntityProblem p JOIN FETCH p.createdBy WHERE UPPER(p.title) LIKE UPPER(CONCAT('%', :text, '%'))")
    List<EntityProblem> findByTitleContains(@Param("text") String text);

    @Query("SELECT p FROM EntityProblem p JOIN FETCH p.createdBy WHERE UPPER(p.tags) LIKE UPPER(CONCAT('%', :text, '%'))")
    List<EntityProblem> findByTagsContains(@Param("text") String text);

    @Query("SELECT p FROM EntityProblem p JOIN FETCH p.createdBy WHERE p.resolved = :resolvedStatus")
    List<EntityProblem> findByResolved(@Param("resolvedStatus") boolean resolvedStatus);

    @Query("SELECT p FROM EntityProblem p WHERE p.createdBy.id = :userId")
    List<EntityProblem> findByUserId(@Param("userId") String userId);
}