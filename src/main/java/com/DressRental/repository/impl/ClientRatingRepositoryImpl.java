package com.DressRental.repository.impl;

import com.DressRental.models.entities.ClientRating;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.ClientRatingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ClientRatingRepositoryImpl extends BaseRepository<ClientRating, UUID> implements ClientRatingRepository {
    @PersistenceContext
    private EntityManager entityManager;
    public ClientRatingRepositoryImpl() {
        super(ClientRating.class);
    }

    @Override
    public boolean existsByRentalId(UUID rentalId) {
        Long count = entityManager.createQuery("select count (r) from ClientRating r where r.rental.id = :rentalId", Long.class)
                .setParameter("rentalId", rentalId)
                .getSingleResult();
        return count > 0;
    }
}
