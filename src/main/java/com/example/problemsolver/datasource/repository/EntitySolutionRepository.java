package com.example.problemsolver.datasource.repository;

import com.example.problemsolver.datasource.entity.EntitySolution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntitySolutionRepository extends JpaRepository<EntitySolution, String> {

    List<EntitySolution> findByCategory(String category);

}