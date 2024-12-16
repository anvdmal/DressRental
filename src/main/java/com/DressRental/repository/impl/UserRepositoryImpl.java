package com.DressRental.repository.impl;

import com.DressRental.models.entities.User;
import com.DressRental.models.entities.Rental;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl extends BaseRepository<User, UUID> implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public List<Rental> getUserRentals(UUID clientId) {
        return entityManager.createQuery("select r from Rental r where r.user.id = :id", Rental.class)
                .setParameter("id", clientId)
                .getResultList();
    }

    @Override
    public BigDecimal getUserRating(UUID clientId) {
        try {
            Double rating = entityManager.createQuery(
                            "select round(avg(ur.rating), 2) from ClientRating ur where ur.user.id = :id", Double.class)
                    .setParameter("id", clientId)
                    .getSingleResult();

            return rating != null ? BigDecimal.valueOf(rating) : BigDecimal.ZERO;
        } catch (NoResultException e) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    @Transactional
    public Optional<User> findByEmail(String email) {
        return entityManager.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        Long count = entityManager.createQuery("select count (u) from User u where u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }
}
