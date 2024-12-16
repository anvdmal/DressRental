package com.DressRental.exceptions;

import java.util.UUID;

public class RentalNotFoundException extends RuntimeException {
    public RentalNotFoundException(UUID id) {
        super("Запись об аренде с идентификатором " + id + " не найдена");
    }
}
