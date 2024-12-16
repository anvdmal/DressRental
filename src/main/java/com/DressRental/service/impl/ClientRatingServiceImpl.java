package com.DressRental.service.impl;

import com.DressRental.dto.ClientRatingCreateEditDTO;
import com.DressRental.dto.ClientRatingDTO;
import com.DressRental.models.entities.Rental;
import com.DressRental.models.entities.User;
import com.DressRental.models.entities.ClientRating;
import com.DressRental.exceptions.InvalidDataException;
import com.DressRental.exceptions.RatingNotFoundException;
import com.DressRental.exceptions.RentalNotFoundException;
import com.DressRental.exceptions.UserNotFoundException;
import com.DressRental.repository.impl.ClientRatingRepositoryImpl;
import com.DressRental.repository.impl.RentalRepositoryImpl;
import com.DressRental.repository.impl.UserRepositoryImpl;
import com.DressRental.service.ClientRatingService;
import com.DressRental.utils.ValidationUtilImpl;
import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientRatingServiceImpl implements ClientRatingService {
    private ClientRatingRepositoryImpl clientRatingRepository;
    private UserRepositoryImpl userRepository;
    private RentalRepositoryImpl rentalRepository;
    private ValidationUtilImpl validationUtil;

    public ClientRatingServiceImpl(ValidationUtilImpl validationUtil) {
        this.validationUtil = validationUtil;
    }

    @Autowired
    private void setClientRatingRepository(ClientRatingRepositoryImpl clientRatingRepository) {
        this.clientRatingRepository = clientRatingRepository;
    }

    @Autowired
    private void setUserRepository(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRentalRepository(RentalRepositoryImpl rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Override
    public ClientRatingDTO getRatingById(UUID ratingId) {
        ClientRating rating = clientRatingRepository.findById(ratingId)
                .orElseThrow(() -> new RatingNotFoundException(ratingId));
        return new ClientRatingDTO(rating.getId(), rating.getUser().getId(), rating.getUser().getName(),
                rating.getRating(), rating.getComment(), rating.getReviewDate());
    }

    @Override
    public void addRating(UUID clientId, UUID rentalId, ClientRatingCreateEditDTO clientRatingDTO) {
        validateUserRating(clientRatingDTO, "Неккоректные данные для добавления рейтинга");

        User user = userRepository.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException(clientId));

        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        ClientRating clientRating = new ClientRating(user, rental, clientRatingDTO.getRating(), clientRatingDTO.getComment());
        clientRatingRepository.create(clientRating);
    }

    @Override
    public boolean hasRatingForRental(UUID rentalId) {
        return clientRatingRepository.existsByRentalId(rentalId);
    }

    @Override
    public List<ClientRatingDTO> getAllRatings() {
        List<ClientRatingDTO> allDresses = clientRatingRepository.findAll()
                .stream()
                .map(r -> new ClientRatingDTO(r.getId(), r.getUser().getId(), r.getUser().getName(),
                        r.getRating(), r.getComment(), r.getReviewDate()))
                .toList();

        if (allDresses.isEmpty()) {
            throw new RatingNotFoundException();
        } else {
            return allDresses;
        }
    }

    @Override
    public void updateRating(UUID userRatingId, ClientRatingCreateEditDTO clientRatingDTO) {
        validateUserRating(clientRatingDTO, "Неккоректные данные для обновления рейтинга");

        ClientRating clientRating = clientRatingRepository.findById(userRatingId)
                .orElseThrow(() -> new RatingNotFoundException(userRatingId));

        clientRating.setRating(clientRatingDTO.getRating());
        clientRating.setComment(clientRatingDTO.getComment());

        clientRatingRepository.update(clientRating);
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
