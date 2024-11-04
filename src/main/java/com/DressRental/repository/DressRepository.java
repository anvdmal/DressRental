package com.DressRental.repository;

import com.DressRental.entity.Dress;
import com.DressRental.entity.DressSize;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DressRepository {
    List<Dress> findDressesByCategoryId(UUID categoryId);
    List<DressSize> findAvailableSizes(String dressName);
    boolean isDressAvailable(UUID dressId, LocalDate startDate, LocalDate endDate);
    List<Dress> findDressesNotRentedFewMonths(int monthQuantity);
    List<Dress> findDressesNeverRented();
    //УДАЛЕНИЕ-?
}


