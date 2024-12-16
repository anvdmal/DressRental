package com.DressRental.service.impl;

import com.DressRental.dto.*;
import com.DressRental.models.entities.Dress;
import com.DressRental.models.entities.DressCategory;
import com.DressRental.models.entities.DressSize;
import com.DressRental.exceptions.CategoryNotFoundException;
import com.DressRental.exceptions.DressNotFoundException;
import com.DressRental.exceptions.SizeNotFoundException;
import com.DressRental.exceptions.InvalidDataException;
import com.DressRental.repository.impl.DressCategoryRepositoryImpl;
import com.DressRental.repository.impl.DressRepositoryImpl;
import com.DressRental.repository.impl.DressSizeRepositoryImpl;
import com.DressRental.service.DressService;
import com.DressRental.utils.ValidationUtilImpl;
import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@EnableCaching
public class DressServiceImpl implements DressService {
    private DressRepositoryImpl dressRepository;
    private DressSizeRepositoryImpl dressSizeRepository;
    private DressCategoryRepositoryImpl dressCategoryRepository;
    private RentalServiceImpl rentalService;
    private ValidationUtilImpl validationUtil;

    public DressServiceImpl(ValidationUtilImpl validationUtil) {
        this.validationUtil = validationUtil;
    }

    @Autowired
    private void setDressRepository(DressRepositoryImpl dressRepository) {
        this.dressRepository = dressRepository;
    }

    @Autowired
    private void setDressCategoryRepository(DressCategoryRepositoryImpl dressCategoryRepository) {
        this.dressCategoryRepository = dressCategoryRepository;
    }

    @Autowired
    private void setDressSizeRepository(DressSizeRepositoryImpl dressSizeRepository) {
        this.dressSizeRepository = dressSizeRepository;
    }

    @Autowired
    public void setRentalService(RentalServiceImpl rentalService) {
        this.rentalService = rentalService;
    }

    @Override
    public void addDress(DressCreateDTO dressDTO) {
        validateDress(dressDTO, "Неккоректные данные для добавления платья");

        DressCategory category = dressCategoryRepository.findByName(dressDTO.getCategory())
                .orElseThrow(() -> new InvalidDataException("Категория не найдена!"));

        for (String sizeName : dressDTO.getSize()) {
            DressSize size = dressSizeRepository.findByName(sizeName)
                    .orElseThrow(() -> new InvalidDataException("Размер " + sizeName + " не найден!"));

            Dress newDress = new Dress(category, dressDTO.getName(), size, dressDTO.getPrice(), dressDTO.getDescription());
            dressRepository.create(newDress);
        }
    }

    @Override
    public DressEditDTO getDressByName(String dressName) {
        List<Dress> dresses = dressRepository.findAllByNameWithSizes(dressName);
        if (dresses.isEmpty()) {
            throw new DressNotFoundException(dressName);
        }

        Dress currentDress = dresses.get(0);

        return new DressEditDTO(currentDress.getCategory().getName(), currentDress.getName(), currentDress.getPrice(), currentDress.getDescription());
    }

    @Override
    public List<DressDTO> getDressesByCategory(String category, UUID userId) {
        List<Dress> dresses = dressRepository.findNotDeletedDressesByCategory(category);
        if (dresses.isEmpty()) {
            throw new DressNotFoundException();
        }

        return dresses.stream()
                .collect(Collectors.groupingBy(Dress::getName))
                .values().stream()
                .map(dressGroup -> getUniqDressWithSizes(dressGroup, userId))
                .toList();
    }

    public List<DressDTO> getAllDressesByCategory(String category, UUID userId) {
        List<Dress> dresses = dressRepository.findAllDressesByCategory(category);
        if (dresses.isEmpty()) {
            throw new DressNotFoundException();
        }

        return dresses.stream()
                .collect(Collectors.groupingBy(Dress::getName))
                .values().stream()
                .map(dressGroup -> getUniqDressWithSizes(dressGroup, userId))
                .toList();
    }

    @Override
    public List<DressDTO> timeToShineDresses(UUID userId) {
        List<DressDTO> timeToShineDresses = new ArrayList<>();
        timeToShineDresses.addAll(dressRepository.findDressesNotRentedFewMonths(2).stream()
                .collect(Collectors.groupingBy(Dress::getName))
                .values().stream()
                .map(dressGroup -> getUniqDressWithSizes(dressGroup, userId))
                .toList());

        timeToShineDresses.addAll(dressRepository.findDressesNeverRented().stream()
                .collect(Collectors.groupingBy(Dress::getName))
                .values().stream()
                .map(dressGroup -> getUniqDressWithSizes(dressGroup, userId))
                .toList());

        return timeToShineDresses;
    }

    private DressDTO getUniqDressWithSizes(List<Dress> dressGroup, UUID userId) {
        Dress firstDress = dressGroup.get(0);
        int deposit = rentalService.calculateDeposit(userId, firstDress.getPrice());

        List<String> uniqueSizes = dressGroup.stream()
                .map(Dress::getSize)
                .map(DressSize::toString)
                .toList();

        return new DressDTO(firstDress.getImageURL(), firstDress.getName(), firstDress.getDescription(),
                firstDress.getPrice(), deposit, uniqueSizes, firstDress.isDeleted());
    }

    @Override
    public List<String> getAvailableDressSize(String dressName) {
        List<String> availableDressSize = dressRepository.findAvailableSizes(dressName)
                .stream()
                .map(DressSize::getName)
                .toList();

        if (availableDressSize.isEmpty()) {
            throw new SizeNotFoundException();
        } else {
            return availableDressSize;
        }
    }

    @Override
    public void updateDress(String dressName, DressEditDTO dressEditDTO) {
        validateDress(dressEditDTO, "Некорректные данные для обновления платья");

        List<Dress> dressesToUpdate = dressRepository.findAllByNameWithSizes(dressName);

        if (dressesToUpdate.isEmpty()) {
            throw new DressNotFoundException(dressName);
        }

        DressCategory dressCategory = dressCategoryRepository.findByName(dressEditDTO.getCategory())
                .orElseThrow(() -> new InvalidDataException("Категория '" + dressEditDTO.getCategory() + "' не найдена!"));

        for (Dress dress : dressesToUpdate) {
            dress.setCategory(dressCategory);
            dress.setName(dressEditDTO.getName());
            dress.setPrice(dressEditDTO.getPrice());
            dress.setDescription(dressEditDTO.getDescription());

            dressRepository.update(dress);
        }
    }

    @Override
    public void deleteDress(String dressName) {
        List<Dress> dressesToDelete = dressRepository.findAllByNameWithSizes(dressName);
        for (Dress dress : dressesToDelete) {
            dress.setDeleted(true);
            dressRepository.update(dress);
        }
    }

    @Override
    @Cacheable("categories")
    public List<String> getAllCategories() {
        List<String> allCategories = dressCategoryRepository.findAll()
                .stream()
                .map(DressCategory::getName)
                .toList();

        if (allCategories.isEmpty()) {
            throw new CategoryNotFoundException();
        } else {
            return allCategories;
        }
    }

    @Override
    @Cacheable("sizes")
    public List<String> getAllSizes() {
        List<String> allSizes = dressSizeRepository.findAll()
                .stream()
                .map(DressSize::getName)
                .toList();

        if (allSizes.isEmpty()) {
            throw new SizeNotFoundException();
        } else {
            return allSizes;
        }
    }

    private <T> void validateDress(T dressDTO, String exceptionMessage) {
        if (!this.validationUtil.isValid(dressDTO)) {
            this.validationUtil
                    .violations(dressDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            throw new InvalidDataException(exceptionMessage);
        }
    }
}
