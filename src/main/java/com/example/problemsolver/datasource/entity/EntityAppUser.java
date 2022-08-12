package com.example.problemsolver.datasource.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_users")
public class EntityAppUser {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "app_user_id", updatable = false)
    private String id;
    @Column(name = "username", unique = true, length = 100)
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "registration_date")
    @CreationTimestamp
    private LocalDateTime registrationDate;
    @Column(name = "active")
    private boolean active;
    @Column(name = "suspended_until")
    private LocalDateTime suspendedUntil;
    @ManyToMany(
            cascade = {CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "app_user_app_role",
            joinColumns = @JoinColumn(name = "fk_app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "fk_app_role_id")
    )
    private Set<EntityAppRole> appRoles;
    @OneToOne(
            cascade = {CascadeType.DETACH, CascadeType.REFRESH}
    )
    @JoinColumn(name = "fk_app_user_details_id", table = "app_users")
    private EntityAppUserDetails appUserDetails;

    public Set<EntityAppRole> getAppRoles() {
        if(appRoles == null) appRoles = new HashSet<>();
        return appRoles;
    }

    public void setAppRoles(Set<EntityAppRole> appRoles) {
        if(appRoles == null) appRoles = new HashSet<>();
        if(appRoles.isEmpty()){
            if(this.appRoles != null){
                this.appRoles.forEach(entityAppRole -> entityAppRole.getAppUsers().remove(this));
            }
        }else {
            appRoles.forEach(entityAppRole -> entityAppRole.getAppUsers().add(this));
        }
        this.appRoles = appRoles;
    }

    @Override
    public String toString() {
        return "EntityAppUser{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityAppUser that = (EntityAppUser) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
