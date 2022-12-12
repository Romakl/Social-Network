package com.romankliuiev.socialnetwork.facade.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(org.springframework.http.HttpStatus.UNAUTHORIZED)
public class NullTokenException extends RuntimeException {
    // for no token response status 401

    public NullTokenException(String message) {
        super(message);
    }
}
