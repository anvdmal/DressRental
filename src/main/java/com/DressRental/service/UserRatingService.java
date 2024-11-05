package com.DressRental.service;

import com.DressRental.dto.UserEmailDTO;
import com.DressRental.dto.UserRatingDTO;

import java.util.List;

public interface UserRatingService {
    void addRating(UserRatingDTO userRatingDTO);
    List<UserRatingDTO> findRatingsByUserEmail(UserEmailDTO userEmailDTO);
}
