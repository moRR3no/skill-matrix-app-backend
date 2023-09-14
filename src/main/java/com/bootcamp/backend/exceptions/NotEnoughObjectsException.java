package com.bootcamp.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughObjectsException extends RuntimeException {
    public NotEnoughObjectsException(String message) { super(message); }
}
