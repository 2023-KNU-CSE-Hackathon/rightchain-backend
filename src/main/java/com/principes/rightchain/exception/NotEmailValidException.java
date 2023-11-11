package com.principes.rightchain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Not Email Valid Exception")
public class NotEmailValidException extends RuntimeException {
    public NotEmailValidException(String message) {
        super(message);
    }
}
