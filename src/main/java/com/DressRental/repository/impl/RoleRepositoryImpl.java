package com.DressRental.repository.impl;

import com.DressRental.entity.Role;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.RoleRepository;

import java.util.UUID;

public class RoleRepositoryImpl extends BaseRepository<Role, UUID> implements RoleRepository {
    public RoleRepositoryImpl() {
        super(Role.class);
    }
}
