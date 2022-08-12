package com.example.problemsolver.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SecurityConstants {
    private final String jwt_key;
    private final String id;
    private final String username;
    private final String userDetailsId;
    private final String password;
    private final String email;
    private final String active;
    private final String authorities;
    private final String authorization = "Authorization";
    private final String bearer = "Bearer ";
}
