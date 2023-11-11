package com.principes.rightchain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Not Email Verified Exception")
public class NotEmailVerifiedException extends RuntimeException {
    public NotEmailVerifiedException(String message) {
        super(message);
    }
}
