package com.microservice.user.domain.exception;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(String nickname) {
        super("User not found: " + nickname);
    }

}
