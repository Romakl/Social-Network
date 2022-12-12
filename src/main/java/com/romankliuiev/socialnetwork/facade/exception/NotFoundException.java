package com.romankliuiev.socialnetwork.facade.exception;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND, reason = "User not found")
public class NotFoundException extends RuntimeException{

    public NotFoundException(String message) {
        super(message);
    }
}
