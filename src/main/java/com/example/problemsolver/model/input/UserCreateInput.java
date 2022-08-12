package com.example.problemsolver.model.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateInput {
    private String username;
    private String password;
    private String passwordConfirm;
    private UserDetailsCreateInput userDetails;
}
