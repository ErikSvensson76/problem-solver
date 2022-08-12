package com.example.problemsolver.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solution implements SearchableItem{
    private String id;
    private OffsetDateTime createDateTime;
    private String prettyCreateDateTime;
    private OffsetDateTime updateDateTime;
    private String prettyUpdateDateTime;
    private SolutionCategory category;
    private String content;
    private User author;
}
