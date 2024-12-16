package com.DressRental.models.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "sizes")
public class DressSize extends BaseEntity {
    private String name;
    private List<Dress> dresses;

    public DressSize(String name) {
        this.name = name;
    }

    protected DressSize() {
    }

    @Column(name = "name", nullable = false, length = 127)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "size", targetEntity = Dress.class)
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
