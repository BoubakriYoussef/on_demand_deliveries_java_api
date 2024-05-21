package com.example.ondemand.exceptions;

public class NoAvailableDriversException extends RuntimeException {
    public NoAvailableDriversException(String message) {
        super(message);
    }
}
