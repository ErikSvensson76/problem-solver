package com.example.problemsolver.datasource.service.interfaces;

public interface GenericMutationCrud <T, ID>{
    T create(T data);
    T findById(ID id);
    T update(T data);
    Integer remove(T data);
}
