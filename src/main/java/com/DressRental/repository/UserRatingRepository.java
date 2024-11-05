package com.DressRental.repository;

import com.DressRental.entity.UserRating;

import java.util.List;

public interface UserRatingRepository {
    List<UserRating> findUserRatingByEmail(String email);
}
