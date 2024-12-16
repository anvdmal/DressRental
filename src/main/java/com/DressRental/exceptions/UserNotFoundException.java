package com.DressRental.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("Пользователь с идентификатором " + id + " не найден");
    }

    public UserNotFoundException(String email) {
        super("Пользователь с email " + email + " не найден");
    }
}
