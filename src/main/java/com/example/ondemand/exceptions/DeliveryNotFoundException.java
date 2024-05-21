package com.example.ondemand.exceptions;

public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException(String message) {
        super(message);
    }
}