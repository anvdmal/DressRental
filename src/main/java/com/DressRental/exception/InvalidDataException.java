package com.DressRental.exception;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException() {
        super("данные введены некорректно!");
    }
}
