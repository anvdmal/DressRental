package com.DressRental.service;

import com.DressRental.dto.input.ClientRatingCreateEditDTO;
import com.DressRental.dto.output.ClientRatingDTO;

import java.util.List;
import java.util.UUID;

public interface ClientRatingService {
    ClientRatingDTO getRatingById(UUID ratingId);
    void addRating(UUID clientId, UUID rentalId, ClientRatingCreateEditDTO clientRatingDTO);
    boolean hasRatingForRental(UUID rentalId);
    List<ClientRatingDTO> getAllRatings();
    void updateRating(UUID userRatingId, ClientRatingCreateEditDTO clientRatingDTO);
}
