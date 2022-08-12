package com.example.problemsolver.datasource.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZoneId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_details")
public class EntityAppUserDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "app_user_details_id", updatable = false)
    private String id;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "country")
    private String country;
    @Column(name = "zone_id")
    private ZoneId localZoneId;

}
