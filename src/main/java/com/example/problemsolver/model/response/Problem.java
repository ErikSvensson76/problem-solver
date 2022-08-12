package com.example.problemsolver.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Problem implements SearchableItem{
    private String id;
    private String title;
    private String content;
    private OffsetDateTime createDateTime;
    private String prettyCreateDateTime;
    private List<String> tags;
    private Integer solutionCount;
    private Boolean resolved;
    private Solution bestSolution;
    private User author;
    private List<Solution> solutions;
}
