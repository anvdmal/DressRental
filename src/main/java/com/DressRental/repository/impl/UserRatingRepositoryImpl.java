package com.DressRental.repository.impl;

import com.DressRental.entity.UserRating;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.UserRatingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserRatingRepositoryImpl extends BaseRepository<UserRating, UUID> implements UserRatingRepository {
    @PersistenceContext
    private EntityManager entityManager;
    public UserRatingRepositoryImpl() {
        super(UserRating.class);
    }

    @Override
    public List<UserRating> findUserRatingByEmail(String email) {
        return entityManager.createQuery("select ur from UserRating ur where ur.user.email = :email", UserRating.class)
                .setParameter("email", email)
                .getResultList();
    }
}
