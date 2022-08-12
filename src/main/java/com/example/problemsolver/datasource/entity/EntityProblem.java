package com.example.problemsolver.datasource.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "problems")
public class EntityProblem {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "problem_id", updatable = false)
    private String id;
    @Column(name = "title")
    private String title;
    @Column(name = "content", length = 1000)
    private String content;
    @Column(name = "tags")
    private String tags;
    @Column(name = "resolved")
    private boolean resolved;
    @Column(name = "create_date_time")
    @CreationTimestamp
    private LocalDateTime createDateTime;
    @ManyToOne(
            cascade = {CascadeType.DETACH, CascadeType.REFRESH}
    )
    @JoinColumn(name = "fk_best_solution_id", table = "problems")
    private EntitySolution bestSolution;

    @OneToMany(
            cascade = {CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.LAZY,
            mappedBy = "problem"
    )
    private List<EntitySolution> proposedSolutions;

    @ManyToOne(
            cascade = {CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "fk_app_user_id", table = "problems")
    private EntityAppUser createdBy;

}
