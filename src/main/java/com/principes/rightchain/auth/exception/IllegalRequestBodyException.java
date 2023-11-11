package com.principes.rightchain.auth.exception;

public class IllegalRequestBodyException extends RuntimeException{
    public IllegalRequestBodyException(String message) {
        super(message);
    }
}
