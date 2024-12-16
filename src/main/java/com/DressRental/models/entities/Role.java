package com.DressRental.models.entities;

import com.DressRental.models.enums.UserRoles;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    private UserRoles name;
    private List<User> users;

    public Role(UserRoles name) {
        this.name = name;
    }

    protected Role() {
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, length = 50)
    public UserRoles getName() {
        return name;
    }

    public void setName(UserRoles name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}