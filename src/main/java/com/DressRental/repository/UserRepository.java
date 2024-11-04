package com.DressRental.repository;

import com.DressRental.entity.Rental;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface UserRepository {
    List<Rental> getUserRentals(UUID clientId);
    BigDecimal getUserRating(UUID clientId);
}
