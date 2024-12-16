package com.DressRental.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException() {
        super("Пользователь с таким email уже существует");
    }
}
