package com.microservice.social_network.infrastructure.adapter.entrypoint.mapper;

import com.microservice.social_network.domain.model.Post;
import com.microservice.social_network.infrastructure.adapter.entrypoint.dto.PostRequest;
import com.microservice.social_network.infrastructure.adapter.entrypoint.dto.PostResponse;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public Post toDomain(PostRequest request) {
        return new Post(
                request.message(),
                Long.valueOf(request.userId())
        );
    }

    public PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getMessage(),
                post.getUserId(),
                post.getCreatedAt(),
                post.getLikes()
        );
    }
}
