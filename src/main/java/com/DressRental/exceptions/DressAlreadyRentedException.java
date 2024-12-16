package com.DressRental.exceptions;

public class DressAlreadyRentedException extends RuntimeException {
    public DressAlreadyRentedException() {
        super("К сожалению, платье недоступно для бронирования в выбранные даты");
    }
}
