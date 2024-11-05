package com.DressRental.exception;

public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException() {
        super("Пользователь не найден!");
    }
}
