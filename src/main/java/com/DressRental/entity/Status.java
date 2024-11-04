package com.DressRental.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "rental_status")
public class Status extends BaseEntity {
    private String status;
    private List<Rental> rentals;

    public Status(String status) {
        this.status = status;
    }

    protected Status() {}

    @Column(name = "status", nullable = false, length = 127)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @OneToMany(mappedBy = "status", targetEntity = Rental.class)
    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }
}
