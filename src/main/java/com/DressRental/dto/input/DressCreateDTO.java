package com.DressRental.dto.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public class DressCreateDTO {
    private String category;
    private String name;
    private List<String> size;
    private Integer price;
    private String description;

    public DressCreateDTO(String category, String name, List<String> size, Integer price, String description) {
        this.category = category;
        this.name = name;
        this.size = size;
        this.price = price;
        this.description = description;
    }

    public DressCreateDTO() {}

    @NotEmpty(message = "Категория обязательна")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @NotEmpty(message = "Наименование обязательно")
    @Length(min = 2, message = "Наименование должно содержать минимум два символа")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = "Размер обязателен")
    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    @NotNull(message = "Цена обязательна")
    @Min(value = 1, message = "Цена должна быть больше 0")
    @Max(value = 50000, message = "Цена не должна превышать 50000")
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @NotEmpty(message = "Описание обязательно")
    @Length(min = 10,message = "Описание слишком короткое")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
