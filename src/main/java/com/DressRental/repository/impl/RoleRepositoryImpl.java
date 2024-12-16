package com.DressRental.repository.impl;

import com.DressRental.models.entities.Role;
import com.DressRental.models.enums.UserRoles;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RoleRepositoryImpl extends BaseRepository<Role, UUID> implements RoleRepository {
    @PersistenceContext
    private EntityManager entityManager;
    public RoleRepositoryImpl() {
        super(Role.class);
    }

    @Override
    public Optional<Role> findRoleByName(UserRoles name) {
        List<Role> results = entityManager.createQuery("select r from Role r where r.name = :name", Role.class)
                .setParameter("name", name)
                .getResultList();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
