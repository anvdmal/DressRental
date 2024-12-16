package com.DressRental.repository;

import com.DressRental.models.entities.Role;
import com.DressRental.models.enums.UserRoles;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findRoleByName(UserRoles name);
}
