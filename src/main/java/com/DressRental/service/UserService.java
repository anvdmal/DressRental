package com.DressRental.service;

import com.DressRental.dto.output.RentalHistoryDTO;
import com.DressRental.dto.output.UserDTO;
import com.DressRental.dto.input.UserEditDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO getUserById(UUID userId);
    void updateUser(String email, UserEditDTO userEditDTO);
    BigDecimal getAverageRatingForUser(UUID clientId);
    List<RentalHistoryDTO> getRentalHistory(UUID clientId);
}
