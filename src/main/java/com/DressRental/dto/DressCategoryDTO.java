package com.DressRental.dto;

public class DressCategoryDTO {
    String name;

    public DressCategoryDTO(String name) {
        this.name = name;
    }

    public DressCategoryDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
