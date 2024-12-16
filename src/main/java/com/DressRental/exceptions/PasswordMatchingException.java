package com.DressRental.exceptions;

public class PasswordMatchingException extends RuntimeException{
    public PasswordMatchingException() {
        super("Пароли не совпадают");
    }
}
