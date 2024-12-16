package com.DressRental.exceptions;

public class SizeNotFoundException extends RuntimeException {
    public SizeNotFoundException() {
        super("Список размеров пуст");
    }
}
