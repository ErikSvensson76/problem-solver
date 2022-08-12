package com.example.problemsolver.datasource.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "solutions")
public class EntitySolution {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "solution_id", updatable = false)
    private String id;
    @Column(name = "category")
    private String category;
    @Column(name = "create_date_time")
    @CreationTimestamp
    private LocalDateTime createDateTime;
    @Column(name = "update_date_time")
    @UpdateTimestamp
    private LocalDateTime updateDateTime;
    @Column(name = "up_votes")
    private int upVotes;
    @Column(name = "down_votes")
    private int downVotes;
    @Column(name = "content", length = 1000)
    private String content;
    @ManyToOne(
            cascade = {CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "fk_app_user_id", table = "solutions")
    private EntityAppUser createdBy;
    @ManyToOne(
            cascade = {CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "fk_problem_id", table = "solutions")
    private EntityProblem problem;

}
