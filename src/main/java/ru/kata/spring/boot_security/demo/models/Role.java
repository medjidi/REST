package ru.kata.spring.boot_security.demo.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "roles")
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@roleId")
public class Role implements GrantedAuthority  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Basic(optional = false)
    private Long id;
    private String name;

    @Transient
    @ManyToMany(mappedBy = "roles")
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public Role(Long id) {
        this.id = id;
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName(){
        return name;
    }
    public String getName() {
        System.out.println(this.id);
        System.out.println(this.name);

        return name.split("_")[1];
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public String toString() {
        return name.split("_")[1];
    }
}
