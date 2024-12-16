package com.DressRental.service.impl;

import com.DressRental.dto.RentalCreateDTO;
import com.DressRental.dto.RentalHistoryDTO;
import com.DressRental.models.entities.Dress;
import com.DressRental.models.entities.Rental;
import com.DressRental.models.entities.Status;
import com.DressRental.models.entities.User;
import com.DressRental.exceptions.*;
import com.DressRental.repository.impl.DressRepositoryImpl;
import com.DressRental.repository.impl.RentalRepositoryImpl;
import com.DressRental.repository.impl.StatusRepositoryImpl;
import com.DressRental.repository.impl.UserRepositoryImpl;
import com.DressRental.service.RentalService;
import com.DressRental.utils.ValidationUtilImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@EnableCaching
public class RentalServiceImpl implements RentalService {
    private RentalRepositoryImpl rentalRepository;
    private UserRepositoryImpl userRepository;
    private DressRepositoryImpl dressRepository;
    private StatusRepositoryImpl statusRepository;
    private UserServiceImpl userService;
    private ValidationUtilImpl validationUtil;

    public RentalServiceImpl(ValidationUtilImpl validationUtil) {
        this.validationUtil = validationUtil;
    }

    @Autowired
    public void setRentalRepository(RentalRepositoryImpl rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Autowired
    public void setUserRepository(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setDressRepository(DressRepositoryImpl dressRepository) {
        this.dressRepository = dressRepository;
    }

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Autowired
    public void setStatusRepository(StatusRepositoryImpl statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    @CacheEvict(cacheNames = "rental_history", allEntries = true)
    public UUID addRental(UUID userId, String dressName, RentalCreateDTO rentalCreateDTO) {
        validateRental(rentalCreateDTO, "Некорректные данные для создания записи о бронировании");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Dress dress = dressRepository.findDressByNameAndSize(dressName, rentalCreateDTO.getDressSize());

        if (!dressRepository.isDressAvailable(dress.getId(), rentalCreateDTO.getRentalDate(), rentalCreateDTO.getReturnDate())) {
            throw new DressAlreadyRentedException();
        }

        int deposit = calculateDeposit(userId, dress.getPrice());
        int finalPrice = deposit + (dress.getPrice() * (int) ChronoUnit.DAYS.between(rentalCreateDTO.getRentalDate(), rentalCreateDTO.getReturnDate())); //1 вкл, 2 не вкл

        Status rentalStatus = statusRepository.findByName("Ожидает примерки")
                .orElseThrow(() -> new InvalidDataException("Статус не найден!"));

        Rental rental = new Rental(user, dress, rentalCreateDTO.getRentalDate(), rentalCreateDTO.getReturnDate(), deposit, finalPrice, rentalStatus);
        rentalRepository.create(rental);

        return rental.getId();
    }

    public int calculateDeposit(UUID userId, int dressPrice) {
        BigDecimal rating = userService.getAverageRatingForUser(userId);

        double multiplier;

        if (rating.compareTo(new BigDecimal("4")) >= 0) {
            multiplier = 0.5;
        } else if (rating.compareTo(new BigDecimal("3")) >= 0) {
            multiplier = 0.75;
        } else {
            multiplier = 1;
        }

        return (int) Math.round(dressPrice * multiplier);
    }

    @Override
    public RentalHistoryDTO getRentalById(UUID rentalId) {
        return rentalRepository.findById(rentalId).map(r -> new RentalHistoryDTO(
                r.getId(),
                r.getUser().getId(),
                r.getDress().getName(),
                r.getDress().getSize().getName(),
                r.getRentalDate(),
                r.getReturnDate(),
                r.getDeposit(),
                r.getFinalPrice(),
                r.getStatus().getName()))
                .orElseThrow(() -> new RentalNotFoundException(rentalId));
    }

    @Override
    public List<RentalHistoryDTO> getNotConfirmedRentals() {
        return getRentalsByStatus("Ожидает примерки");
    }

    @Override
    public List<RentalHistoryDTO> getActiveRentals() {
        return getRentalsByStatus("Активно");
    }

    @Override
    @Cacheable("completed_rentals")
    public List<RentalHistoryDTO> getCompletedRentals() {
        return getRentalsByStatus("Завершено");
    }

    private List<RentalHistoryDTO> getRentalsByStatus(String statusName) {
        statusRepository.findByName(statusName)
                .orElseThrow(() -> new InvalidDataException("Статус '" + statusName + "' не найден!"));

        List<Rental> rentals = rentalRepository.findRentalsByStatus(statusName);

        return rentals.stream()
                .map(r -> new RentalHistoryDTO(r.getId(), r.getUser().getId(), r.getDress().getName(), r.getDress().getSize().getName(),
                        r.getRentalDate(), r.getReturnDate(), r.getDeposit(), r.getFinalPrice(), r.getStatus().getName()))
                .toList();
    }

    @Override
    public void confirmRental(UUID rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        Status activeStatus = statusRepository.findByName("Активно")
                .orElseThrow(() -> new InvalidDataException("Статус 'Активно' не найден!"));

        rental.setStatus(activeStatus);
        rentalRepository.update(rental);
    }

    @Override
    public void cancelRental(UUID rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        Status cancelStatus = statusRepository.findByName("Отменено")
                .orElseThrow(() -> new InvalidDataException("Статус 'Отменено' не найден!"));

        rental.setStatus(cancelStatus);
        rentalRepository.update(rental);
    }

    @Override
    @CacheEvict(cacheNames = "completed_rentals", allEntries = true)
    public void completeRental(UUID rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        Status completeStatus = statusRepository.findByName("Завершено")
                .orElseThrow(() -> new InvalidDataException("Статус 'Завершено' не найден!"));

        rental.setStatus(completeStatus);
        rentalRepository.update(rental);
    }

    private void validateRental(RentalCreateDTO rentalCreateDTO, String exceptionMessage) {
        if (!validationUtil.isValid(rentalCreateDTO)) {
            validationUtil
                    .violations(rentalCreateDTO)
                    .forEach(violation -> System.out.println(violation.getMessage()));
            throw new InvalidDataException(exceptionMessage);
        }
    }
}
