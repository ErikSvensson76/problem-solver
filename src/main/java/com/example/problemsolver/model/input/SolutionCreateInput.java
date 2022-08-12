package com.example.problemsolver.model.input;

import com.example.problemsolver.model.response.SolutionCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolutionCreateInput {
    private String content;
    private SolutionCategory category;
    private String problemId;
}
