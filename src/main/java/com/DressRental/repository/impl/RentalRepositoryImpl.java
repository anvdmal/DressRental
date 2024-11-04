package com.DressRental.repository.impl;

import com.DressRental.entity.Rental;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.RentalRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public class RentalRepositoryImpl extends BaseRepository<Rental, UUID> implements RentalRepository {
    public RentalRepositoryImpl() {
        super(Rental.class);
    }
}
