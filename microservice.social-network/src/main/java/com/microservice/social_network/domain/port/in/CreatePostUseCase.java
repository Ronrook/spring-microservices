package com.microservice.social_network.domain.port.in;

import com.microservice.social_network.domain.model.Post;

public interface CreatePostUseCase {

    Post create(Post post);

}
