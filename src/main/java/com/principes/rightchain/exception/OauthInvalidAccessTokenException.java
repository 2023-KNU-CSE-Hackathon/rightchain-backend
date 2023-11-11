package com.principes.rightchain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 Access Token")
public class OauthInvalidAccessTokenException extends RuntimeException {
    public OauthInvalidAccessTokenException(String message) {
        super(message);
    }
}
