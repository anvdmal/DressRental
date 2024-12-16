package com.DressRental.dto;

import com.DressRental.utils.UniqueEmail;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class UserSignUpDTO {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;

    public UserSignUpDTO(String name, String email, String password, String confirmPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public UserSignUpDTO() {}

    @NotEmpty(message = "Имя обязательно")
    @Length(min = 2, max = 40, message = "Имя должно содержать минимум два символа")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = "Email обязателен")
    @UniqueEmail(message = "Пользователь с таким email уже существует")
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

    @NotEmpty(message = "Подтверждения пароля обязательно")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}


