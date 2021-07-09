package com.example.demo2.exception;

public class ValidationException extends Exception {

    private String message;

    //Создаем собственное исключение
    public ValidationException(String message) {
    }

    public String getMessage() {
        return message;
    }
}
