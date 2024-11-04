package com.DressRental.repository.impl;

import com.DressRental.entity.UserRating;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.UserRatingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserRatingRepositoryImpl extends BaseRepository<UserRating, UUID> implements UserRatingRepository {
    public UserRatingRepositoryImpl() {
        super(UserRating.class);
    }
}
