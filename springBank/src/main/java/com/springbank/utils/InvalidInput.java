package com.springbank.utils;

public class InvalidInput extends IllegalArgumentException {
    public InvalidInput(String message) {
        super(String.format("Invalid input: %s", message));
    }
}
