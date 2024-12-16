package com.DressRental.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {
        super("Список категорий пуст");
    }
}
