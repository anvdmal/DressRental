package com.DressRental.exceptions;

public class DressNotFoundException extends RuntimeException {
    public DressNotFoundException() {
        super("Платья не найдены");
    }
    public DressNotFoundException(String name) {
        super("Платья с наименованием " + name + " не найдены");
    }
}
