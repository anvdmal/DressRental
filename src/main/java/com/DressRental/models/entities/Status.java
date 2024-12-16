package com.DressRental.models.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "rental_status")
public class Status extends BaseEntity {
    private String name;
    private List<Rental> rentals;

    public Status(String name) {
        this.name = name;
    }

    protected Status() {}

    @Column(name = "name", nullable = false, length = 127)
    public String getName() {
        return name;
    }

    public void setName(String status) {
        this.name = status;
    }

    @OneToMany(mappedBy = "status", targetEntity = Rental.class)
    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }
}
