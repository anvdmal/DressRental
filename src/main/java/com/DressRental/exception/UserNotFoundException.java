package com.DressRental.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Пользователь не найден!");
    }

    public UserNotFoundException(UUID id) {
        super("Пользователь с идентификатором " + id + " не найден!");
    }

    public UserNotFoundException(String email) {
        super("Пользователь с email " + email + " не найден!");
    }
}
