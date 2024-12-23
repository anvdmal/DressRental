package com.DressRental.dto.input;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class UserEditDTO {
    private String name;
    private String password;
    private String confirmPassword;

    public UserEditDTO(String name, String password, String confirmPassword) {
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public UserEditDTO() {}

    @NotEmpty(message = "Имя обязательно")
    @Length(min = 2, max = 40, message = "Имя должно содержать минимум два символа")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
