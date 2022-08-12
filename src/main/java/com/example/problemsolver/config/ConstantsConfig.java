package com.example.problemsolver.config;

import com.example.problemsolver.exception.AppResourceNotFoundException;
import com.example.problemsolver.security.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class ConstantsConfig {

    @Bean
    public SecurityConstants securityConstants(){
        Path path = Paths.get("key/key.txt").toAbsolutePath();
        String line;
        try(BufferedReader reader = Files.newBufferedReader(path.toAbsolutePath())){
            line = reader.readLine();
        }catch (IOException ex){
            throw new AppResourceNotFoundException("Couldn't read data from " + path);
        }

        return new SecurityConstants(
                line,
                "id",
                "username",
                "userDetailsId",
                "password",
                "email",
                "active",
                "authorities"
        );
    }





}
