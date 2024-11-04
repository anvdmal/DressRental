package com.DressRental.repository;

import com.DressRental.entity.Rental;
import com.DressRental.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<Rental> getUserRentals(UUID clientId);
    BigDecimal getUserRating(UUID clientId);
    Optional<User> findByEmail(String email);
}
