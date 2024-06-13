package com.appointment_scheduling_app.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserAlreadyExistsWithEmailException extends RuntimeException{
    public UserAlreadyExistsWithEmailException(String message){
        super(message);
    }
}