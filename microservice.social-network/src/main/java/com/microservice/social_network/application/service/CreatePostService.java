package com.microservice.social_network.application.service;

import com.microservice.social_network.domain.model.Post;
import com.microservice.social_network.domain.port.in.CreatePostUseCase;
import com.microservice.social_network.domain.port.out.PostRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreatePostService implements CreatePostUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreatePostService.class);

    private final PostRepositoryPort postRepository;

    public CreatePostService(PostRepositoryPort postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public Post create(Post post) {

        Post saved = postRepository.save(post);
        log.debug("Post created successfully: {}", saved.getId());

        return saved;
    }
}
