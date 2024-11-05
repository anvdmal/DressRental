package com.DressRental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserEmailDTO {
    private String email;

    public UserEmailDTO(String email) {
        this.email = email;
    }

    public UserEmailDTO() {
    }

    @NotNull
    @NotEmpty
    @Email()
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
