package com.example.problemsolver.datasource.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_roles")
public class EntityAppRole {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "app_role_id", updatable = false)
    private String id;
    @Column(name = "user_role", unique = true)
    private UserRole userRole;
    @ManyToMany(
            cascade = {CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.LAZY,
            mappedBy = "appRoles"
    )
    private Set<EntityAppUser> appUsers;

    public Set<EntityAppUser> getAppUsers() {
        if(appUsers == null) appUsers = new HashSet<>();
        return appUsers;
    }

    public void setAppUsers(Set<EntityAppUser> appUsers) {
        if(appUsers == null) appUsers = new HashSet<>();
        if(appUsers.isEmpty()){
            if(this.appUsers != null){
                this.appUsers.forEach(entityAppUser -> entityAppUser.getAppRoles().remove(this));
            }
        }else{
            appUsers.forEach(entityAppUser -> entityAppUser.getAppRoles().add(this));
        }
        this.appUsers = appUsers;
    }

    @Override
    public String toString() {
        return "EntityAppRole{" +
                "id='" + id + '\'' +
                ", userRole=" + userRole +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityAppRole that = (EntityAppRole) o;
        return userRole == that.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userRole);
    }
}
