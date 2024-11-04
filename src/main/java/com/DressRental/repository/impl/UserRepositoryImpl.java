package com.DressRental.repository.impl;

import com.DressRental.entity.User;
import com.DressRental.entity.Rental;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
        return entityManager.createQuery("select r from Rental r where r.user = :id", Rental.class)
                .setParameter("id", clientId)
                .getResultList();
    }

    @Override
    public BigDecimal getUserRating(UUID clientId) {
        return entityManager.createQuery("select round(avg(ur.rating), 2) from UserRating ur where ur.user = :id", BigDecimal.class)
                .setParameter("id", clientId)
                .getSingleResult();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> results = entityManager.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
