package com.DressRental.dto;

import jakarta.validation.constraints.NotEmpty;
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

    public UserDTO() {}

    @NotEmpty(message = "Email обязателен")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotEmpty(message = "Пароль обязателен")
    @Length(min = 8, message = "Пароль должен состоять минимум из восьми символов")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotEmpty(message = "Имя обязательно")
    @Length(min = 2, max = 40, message = "Имя должно содержать минимум два символа")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
