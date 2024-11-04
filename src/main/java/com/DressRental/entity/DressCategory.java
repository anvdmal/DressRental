package com.DressRental.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categories")
public class DressCategory extends BaseEntity {
    private String category;
    private List<Dress> dresses;

    public DressCategory(String name) {
        this.category = name;
    }

    protected DressCategory() {
    }

    @Column(name = "category", nullable = false, length = 127)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @OneToMany(mappedBy = "category", targetEntity = Dress.class)
    public List<Dress> getDresses() {
        return dresses;
    }

    public void setDresses(List<Dress> dresses) {
        this.dresses = dresses;
    }
}
