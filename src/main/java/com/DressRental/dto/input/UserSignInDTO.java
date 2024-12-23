package com.DressRental.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class UserSignInDTO {
    private String email;
    private String password;

    public UserSignInDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserSignInDTO() {}

    @NotEmpty(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
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
}
