package com.DressRental.service.impl;

import com.DressRental.dto.output.RentalHistoryDTO;
import com.DressRental.dto.output.UserDTO;
import com.DressRental.dto.input.UserEditDTO;
import com.DressRental.exceptions.PasswordMatchingException;
import com.DressRental.models.entities.Rental;
import com.DressRental.models.entities.User;
import com.DressRental.exceptions.InvalidDataException;
import com.DressRental.exceptions.UserNotFoundException;
import com.DressRental.repository.impl.UserRepositoryImpl;
import com.DressRental.service.UserService;
import jakarta.validation.ConstraintViolation;
import com.DressRental.utils.ValidationUtilImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@EnableCaching
public class UserServiceImpl implements UserService {
    private UserRepositoryImpl userRepository;
    private ValidationUtilImpl validationUtil;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(ValidationUtilImpl validationUtil, PasswordEncoder passwordEncoder) {
        this.validationUtil = validationUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private void setUserRepository(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO getUserById(UUID userId) {
        return userRepository.findById(userId).map(u -> new UserDTO(u.getEmail(), u.getPassword(), u.getName()))
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public void updateUser(String email, UserEditDTO userEditDTO) {
        validateUser(userEditDTO, "Некорректные данные для обновления пользователя!");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));


        if (!userEditDTO.getPassword().equals(userEditDTO.getConfirmPassword())) {
            throw new PasswordMatchingException();
        }

        user.setPassword(passwordEncoder.encode(userEditDTO.getPassword()));
        user.setName(userEditDTO.getName());

        userRepository.update(user);
    }

    @Override
    public BigDecimal getAverageRatingForUser(UUID clientId) {
        return userRepository.getUserRating(clientId);
    }

    @Override
    @Cacheable("rental_history")
    public List<RentalHistoryDTO> getRentalHistory(UUID clientId) {
        List<Rental> rentalHistory = userRepository.getUserRentals(clientId);

        return rentalHistory.stream()
                .map(this::mapToRentalDTO)
                .collect(Collectors.toList());
    }

    private RentalHistoryDTO mapToRentalDTO(Rental rental) {
        return new RentalHistoryDTO(
                rental.getId(),
                rental.getUser().getId(),
                rental.getDress().getName(),
                rental.getDress().getSize().getName(),
                rental.getRentalDate(),
                rental.getReturnDate(),
                rental.getDeposit(),
                rental.getFinalPrice(),
                rental.getStatus().getName());
    }

    private <T> void validateUser(T userDTO, String exceptionMessage) {
        if (!this.validationUtil.isValid(userDTO)) {
            this.validationUtil
                    .violations(userDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            throw new InvalidDataException(exceptionMessage);
        }
    }
}
