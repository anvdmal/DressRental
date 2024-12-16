package com.DressRental.repository;

import com.DressRental.models.entities.Dress;
import com.DressRental.models.entities.DressSize;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DressRepository {
    List<Dress> findAllByNameWithSizes(String name);
    List<Dress> findNotDeletedDressesByCategory(String category);
    List<Dress> findAllDressesByCategory(String category);
    List<DressSize> findAvailableSizes(String dressName);
    Dress findDressByNameAndSize(String dressName, String size);
    boolean isDressAvailable(UUID dressId, LocalDate startDate, LocalDate endDate);
    List<Dress> findDressesNotRentedFewMonths(int monthQuantity);
    List<Dress> findDressesNeverRented();
}


