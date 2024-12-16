package com.DressRental.utils;

import com.DressRental.dto.RentalCreateDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ReturnDateValidator implements ConstraintValidator<ReturnDateValid, RentalCreateDTO> {
    @Override
    public boolean isValid(RentalCreateDTO addRentalForm, ConstraintValidatorContext constraintValidatorContext) {
        if (addRentalForm == null) {
            return true;
        }

        return addRentalForm.getReturnDate().isAfter(addRentalForm.getRentalDate());
    }
}
