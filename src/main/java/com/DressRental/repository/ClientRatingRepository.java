package com.DressRental.repository;

import java.util.UUID;

public interface ClientRatingRepository {
    boolean existsByRentalId(UUID rentalId);
}
