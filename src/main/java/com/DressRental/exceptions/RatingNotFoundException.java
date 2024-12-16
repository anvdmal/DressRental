package com.DressRental.exceptions;

import java.util.UUID;

public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException() {
        super("Записи о рейтинге клиента не найдены");
    }

    public RatingNotFoundException(UUID id) {
        super("Запись о рейтинге с идентификатором " + id + " не найдена");
    }
}
