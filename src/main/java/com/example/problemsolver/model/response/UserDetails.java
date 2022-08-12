package com.example.problemsolver.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {
    private String id;
    private String email;
    private URL profile;
    private String displayName;
}
