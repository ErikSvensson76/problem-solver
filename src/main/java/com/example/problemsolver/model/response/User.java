package com.example.problemsolver.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String username;
    private OffsetDateTime registrationDate;
    private UserDetails userDetails;
    private List<Problem> problems;
    private List<String> roles;
}
