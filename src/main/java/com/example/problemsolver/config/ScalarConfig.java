package com.example.problemsolver.config;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class ScalarConfig {
    /*
    @Bean
    public GraphQLScalarType nonNegativeInt(){
        return ExtendedScalars.NonNegativeInt;
    }

    @Bean
    public GraphQLScalarType url(){
        return ExtendedScalars.Url;
    }

    @Bean
    public GraphQLScalarType dateTime(){
        return ExtendedScalars.DateTime;
    }

     */

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(){
        return  wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.NonNegativeInt)
                .scalar(ExtendedScalars.Url)
                .build();
    }
}
