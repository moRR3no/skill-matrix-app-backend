package com.bootcamp.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class WrongInputException extends RuntimeException{
    public WrongInputException(String message) {
        super(message);
    }
}