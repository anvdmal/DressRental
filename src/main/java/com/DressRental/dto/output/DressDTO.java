package com.DressRental.dto.output;

import java.util.List;

public class DressDTO {
    String imageUrl;
    String name;
    String description;
    Integer price;
    Integer deposit;
    List<String> sizes;
    boolean isDeleted;

    public DressDTO(String imageUrl, String name, String description, Integer price, Integer deposit, List<String> sizes, boolean isDeleted) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.price = price;
        this.deposit = deposit;
        this.sizes = sizes;
        this.isDeleted = isDeleted;
    }

    public DressDTO() {}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
