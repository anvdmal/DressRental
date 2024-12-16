package com.DressRental.models.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "dresses")
public class Dress extends BaseEntity {
    private DressCategory category;
    private String name;
    private String imageURL;
    private DressSize size;
    private Integer price;
    private String description;
    private boolean isDeleted;
    private List<Rental> rentals;

    public Dress(DressCategory category, String name, DressSize size, Integer price, String description) {
        this.category = category;
        this.name = name;
        this.size = size;
        this.price = price;
        this.description = description;
        this.isDeleted = false;
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
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "is_deleted", nullable = false)
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @OneToMany(mappedBy = "dress", targetEntity = Rental.class)
    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }
}