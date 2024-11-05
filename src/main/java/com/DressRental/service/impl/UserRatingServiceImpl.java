package com.DressRental.service.impl;

import com.DressRental.dto.UserEmailDTO;
import com.DressRental.dto.UserRatingDTO;
import com.DressRental.entity.User;
import com.DressRental.entity.UserRating;
import com.DressRental.exception.InvalidDataException;
import com.DressRental.exception.UserNotFoundException;
import com.DressRental.repository.impl.UserRatingRepositoryImpl;
import com.DressRental.repository.impl.UserRepositoryImpl;
import com.DressRental.service.UserRatingService;
import com.DressRental.utils.ValidationUtilImpl;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRatingServiceImpl implements UserRatingService {
    private final UserRatingRepositoryImpl userRatingRepository;
    private final UserRepositoryImpl userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtilImpl validationUtil;

    public UserRatingServiceImpl(UserRatingRepositoryImpl userRatingRepository, UserRepositoryImpl userRepository, ModelMapper modelMapper, ValidationUtilImpl validationUtil) {
        this.userRatingRepository = userRatingRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void addRating(UserRatingDTO userRatingDTO) {
        validateUserRating(userRatingDTO, "неккоректные данные для добавления!");

        User user = userRepository.findByEmail(userRatingDTO.getUserEmail())
                .orElseThrow(() -> new UserNotFoundException(userRatingDTO.getUserEmail()));

        UserRating userRating = new UserRating(user, userRatingDTO.getRating(), userRatingDTO.getComment());
        userRating = userRatingRepository.create(userRating);

        modelMapper.map(userRating, UserRatingDTO.class);
    }

    @Override
    public List<UserRatingDTO> findRatingsByUserEmail(UserEmailDTO userEmailDTO) {
        validateUserRating(userEmailDTO, "неккоректный email!");

        User user = userRepository.findByEmail(userEmailDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException(userEmailDTO.getEmail()));

        return userRatingRepository.findUserRatingByEmail(user.getEmail())
                .stream()
                .map(rating -> modelMapper.map(rating, UserRatingDTO.class))
                .collect(Collectors.toList());
    }

    private <T> void validateUserRating(T userRatingDTO, String exceptionMessage) {
        if (!this.validationUtil.isValid(userRatingDTO)) {
            this.validationUtil
                    .violations(userRatingDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            throw new InvalidDataException(exceptionMessage);
        }
    }
}
