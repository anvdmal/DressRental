package com.DressRental.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "sizes")
public class DressSize extends BaseEntity {
    private String size;
    private List<Dress> dresses;

    public DressSize(String size) {
        this.size = size;
    }

    protected DressSize() {
    }

    @Column(name = "size", nullable = false, length = 127)
    public String getSize() {
        return size;
    }

    public void setSize(String name) {
        this.size = name;
    }

    @OneToMany(mappedBy = "size", targetEntity = Dress.class)
    public List<Dress> getDresses() {
        return dresses;
    }

    public void setDresses(List<Dress> dresses) {
        this.dresses = dresses;
    }
}
