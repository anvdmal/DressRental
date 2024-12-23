package com.DressRental.dto.input;

import com.DressRental.utils.ReturnDateValid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@ReturnDateValid
public class RentalCreateDTO {
    private String dressSize;
    private LocalDate rentalDate;
    private LocalDate returnDate;

    public RentalCreateDTO(String dressSize, LocalDate rentalDate, LocalDate returnDate) {
        this.dressSize = dressSize;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
    }

    public RentalCreateDTO() {}

    @NotEmpty(message = "Размер обязателен")
    public String getDressSize() {
        return dressSize;
    }

    public void setDressSize(String dressSize) {
        this.dressSize = dressSize;
    }
    @NotNull(message = "Дата аренды обязательна")
    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }
    @NotNull(message = "Дата возврата обязательна")
    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}