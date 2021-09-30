package com.example.demo2.exception.users;

/**
 * Custom error class.
 *
 * @author Maxim
 * @version 1.0
 */
public class ValidationException extends Exception {

    private String message;

    //Создаем собственное исключение
    /**
     * Method that throws its own exception
     * @param message text exception
     */
    public ValidationException(String message) {
    }

    /**
     * Method that returns the text of the exception message
     * @return text exception
     */
    public String getMessage() {
        return message;
    }
}
