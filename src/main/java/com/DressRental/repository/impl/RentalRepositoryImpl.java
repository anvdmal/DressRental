package com.DressRental.repository.impl;

import com.DressRental.models.entities.Rental;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.RentalRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public class RentalRepositoryImpl extends BaseRepository<Rental, UUID> implements RentalRepository {
    @PersistenceContext
    private EntityManager entityManager;
    public RentalRepositoryImpl() {
        super(Rental.class);
    }

    @Override
    public List<Rental> findRentalsByStatus(String status) {
        return entityManager.createQuery("select r from Rental r where r.status.name = :status", Rental.class)
                .setParameter("status", status)
                .getResultList();
    }
}
