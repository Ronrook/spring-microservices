package com.microservice.social_network.domain.port.in;

import com.microservice.social_network.domain.model.Post;

import java.util.List;

public interface GetPostsUseCase {

    List<Post> findAll();

}
