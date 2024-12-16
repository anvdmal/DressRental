package com.DressRental.repository;

import com.DressRental.models.entities.Rental;

import java.util.List;

public interface RentalRepository {
    List<Rental> findRentalsByStatus(String status);
}
