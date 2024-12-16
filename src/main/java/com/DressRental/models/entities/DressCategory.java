package com.DressRental.models.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categories")
public class DressCategory extends BaseEntity {
    private String name;
    private List<Dress> dresses;

    public DressCategory(String name) {
        this.name = name;
    }

    protected DressCategory() {
    }

    @Column(name = "name", nullable = false, length = 127)
    public String getName() {
        return name;
    }

    public void setName(String category) {
        this.name = category;
    }

    @OneToMany(mappedBy = "category", targetEntity = Dress.class)
    public List<Dress> getDresses() {
        return dresses;
    }

    public void setDresses(List<Dress> dresses) {
        this.dresses = dresses;
    }

    @Override
    public String toString() {
        return name;
    }
}
