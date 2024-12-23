package com.DressRental.dto.output;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class RentalHistoryDTO implements Serializable {
    UUID rentalId;
    UUID clientId;
    private String dressName;
    private String dressSize;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private int deposit;
    private int finalPrice;
    private String status;

    public RentalHistoryDTO(UUID rentalId, UUID clientId, String dressName, String dressSize, LocalDate rentalDate, LocalDate returnDate, int deposit, int finalPrice, String status) {
        this.rentalId = rentalId;
        this.clientId = clientId;
        this.dressName = dressName;
        this.dressSize = dressSize;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.deposit = deposit;
        this.finalPrice = finalPrice;
        this.status = status;
    }

    public RentalHistoryDTO() {}

    public UUID getRentalId() {
        return rentalId;
    }

    public void setRentalId(UUID rentalId) {
        this.rentalId = rentalId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getDressName() {
        return dressName;
    }

    public void setDressName(String dressName) {
        this.dressName = dressName;
    }

    public String getDressSize() {
        return dressSize;
    }

    public void setDressSize(String dressSize) {
        this.dressSize = dressSize;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
