package com.microservice.social_network.domain.port.out;

import com.microservice.social_network.domain.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryPort {

    Post save(Post post);

    List<Post> findAll();

    Optional<Post> findById(Long id);

}
