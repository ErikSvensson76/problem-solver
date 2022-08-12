package com.example.problemsolver.model.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsCreateInput {
    private String email;
    private String displayName;
    private String timeZone;
    private URL avatar;
}
