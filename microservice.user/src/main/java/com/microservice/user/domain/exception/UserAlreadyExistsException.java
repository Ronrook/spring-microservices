package com.microservice.user.domain.exception;



public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException() {
        super("User already exists");
    }

}