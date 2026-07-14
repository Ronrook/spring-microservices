package com.microservice.social_network.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class Post {

    private Long id;
    private String message;
    private Long userId;
    private LocalDateTime createdAt;
    private int likes;


    public Post(
            String message,
            Long userId
    ){

        validateMessage(message);
        validateUserId(userId);

        this.message = message;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.likes = 0;
    }

    public void incrementLikes() {
        this.likes++;
    }

    public void decrementLikes() {
        if (this.likes > 0) {
            this.likes--;
        }
    }


    private void validateMessage(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException(
                    "Message is required"
            );
        }
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID is required"
            );
        }
    }
}
