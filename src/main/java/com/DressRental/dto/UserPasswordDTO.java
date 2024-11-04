package com.DressRental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UserPasswordDTO {
    private String email;
    private String password;

    public UserPasswordDTO(String email, String password) {
        this.email = email;
        this.password = password;
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

    @NotNull
    @NotEmpty
    @Length(min = 8, message = "Пароль должен состоять минимум из восьми символов!")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
