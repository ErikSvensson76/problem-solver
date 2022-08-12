package com.example.problemsolver.exception;

import java.util.function.Supplier;

public class AppResourceNotFoundException extends RuntimeException{

    public AppResourceNotFoundException(String message) {
        super(message);
    }

    public static Supplier<AppResourceNotFoundException> of(Object id, Class<?> clazz){
        return () -> new AppResourceNotFoundException("Couldn't find " + clazz.getSimpleName() + " object with id " + id);
    }

}
