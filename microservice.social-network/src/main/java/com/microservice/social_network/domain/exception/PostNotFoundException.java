package com.microservice.social_network.domain.exception;

public class PostNotFoundException extends BusinessException {

    public PostNotFoundException(Long id) {
        super("Post not found: " + id);
    }

}
