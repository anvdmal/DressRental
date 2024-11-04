package com.DressRental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UserNameDTO {
    private String email;
    private String name;

    public UserNameDTO(String email, String name) {
        this.email = email;
        this.name = name;
    }

    @NotNull
    @NotEmpty
    @Email(message = "Неккоректный email!")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserNameDTO(String name) {
        this.name = name;
    }

    @NotNull
    @NotEmpty
    @Length(min = 2, message = "Имя должно содержать минимум два символа!")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
