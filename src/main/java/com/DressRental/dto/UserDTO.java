package com.DressRental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UserDTO {
    private String email;
    private String password;
    private String name;

    public UserDTO(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    protected UserDTO() {
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
