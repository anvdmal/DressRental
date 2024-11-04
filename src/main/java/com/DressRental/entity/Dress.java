package com.DressRental.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "dresses")
public class Dress extends BaseEntity {
    private DressCategory category;
    private String name;
    private String imageURL;
    private DressSize size;
    private int price; // >0
    private String color;
    private String description;
    private boolean availability;
    private List<Rental> rentals;

    public Dress(DressCategory category, String name, String imageURL, DressSize size,
                 int price, String color, String description) {
        this.category = category;
        this.name = name;
        this.imageURL = imageURL;
        this.size = size;
        this.price = price;
        this.color = color;
        this.description = description;
        this.availability = true;
    }

    protected Dress() {}

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    public DressCategory getCategory() {
        return category;
    }

    public void setCategory(DressCategory category) {
        this.category = category;
    }

    @Column(name = "name", nullable = false, length = 127)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "image_url", length = 511)
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @ManyToOne
    @JoinColumn(name = "size_id", referencedColumnName = "id", nullable = false)
    public DressSize getSize() {
        return size;
    }

    public void setSize(DressSize size) {
        this.size = size;
    }

    @Column(name = "price", nullable = false)
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Column(name = "color", nullable = false, length = 127)
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "availability", nullable = false)
    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    @OneToMany(mappedBy = "dress", targetEntity = Rental.class)
    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }
}