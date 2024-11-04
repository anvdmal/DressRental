package com.DressRental.repository.impl;

import com.DressRental.entity.Role;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.RoleRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RoleRepositoryImpl extends BaseRepository<Role, UUID> implements RoleRepository {
    public RoleRepositoryImpl() {
        super(Role.class);
    }
}
