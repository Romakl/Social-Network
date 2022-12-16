package com.romankliuiev.socialnetwork.facade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "You can't follow yourself")
public class FollowYourselfException extends RuntimeException {
    public FollowYourselfException(String message) {
        super(message);
    }
}
