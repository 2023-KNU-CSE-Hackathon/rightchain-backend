package com.principes.rightchain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 인가 코드")
public class OauthInvalidAuthorizationCodeException extends RuntimeException {
    public OauthInvalidAuthorizationCodeException(String message) {
        super(message);
    }
}
