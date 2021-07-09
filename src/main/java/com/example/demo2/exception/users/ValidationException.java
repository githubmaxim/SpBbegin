package com.example.demo2.exception.users;

public class ValidationException extends Exception {

    private String message;

    //Создаем собственное исключение
    public ValidationException(String message) {
    }

    public String getMessage() {
        return message;
    }
}
