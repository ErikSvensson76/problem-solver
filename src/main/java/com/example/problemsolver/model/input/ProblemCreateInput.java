package com.example.problemsolver.model.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemCreateInput {
    private String title;
    private String content;
    private List<String> tags;
}
