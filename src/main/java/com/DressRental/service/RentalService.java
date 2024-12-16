package com.DressRental.service;

import com.DressRental.dto.RentalCreateDTO;
import com.DressRental.dto.RentalHistoryDTO;

import java.util.List;
import java.util.UUID;

public interface RentalService {
    UUID addRental(UUID userId, String dressName, RentalCreateDTO rentalCreateDTO);
    RentalHistoryDTO getRentalById(UUID rentalId);
    List<RentalHistoryDTO> getNotConfirmedRentals();
    List<RentalHistoryDTO> getActiveRentals();
    List<RentalHistoryDTO> getCompletedRentals();
    void confirmRental(UUID rentalId);
    void cancelRental(UUID rentalId);
    void completeRental(UUID rentalId);
}
