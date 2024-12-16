package com.DressRental.models.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "rental")
public class Rental extends BaseEntity {
    private User user;
    private Dress dress;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private int deposit;
    private int finalPrice;
    private Status status;

    public Rental(User user, Dress dress, LocalDate rentalDate, LocalDate returnDate, int deposit, int finalPrice, Status status) {
        this.user = user;
        this.dress = dress;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.deposit = deposit;
        this.finalPrice = finalPrice;
        this.status = status;
    }

    protected Rental() {}

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "dress_id", referencedColumnName = "id", nullable = false)
    public Dress getDress() {
        return dress;
    }

    public void setDress(Dress dress) {
        this.dress = dress;
    }

    @Column(name = "rental_date", nullable = false)
    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    @Column(name = "return_date", nullable = false)
    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Column(name = "deposit", nullable = false)
    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    @Column(name = "final_price", nullable = false)
    public int getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "id", nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
