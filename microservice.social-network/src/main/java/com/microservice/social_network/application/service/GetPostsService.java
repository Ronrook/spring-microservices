package com.microservice.social_network.application.service;

import com.microservice.social_network.domain.model.Post;
import com.microservice.social_network.domain.port.in.GetPostsUseCase;
import com.microservice.social_network.domain.port.out.PostRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetPostsService implements GetPostsUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetPostsService.class);

    private final PostRepositoryPort postRepository;

    public GetPostsService(PostRepositoryPort postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findAll() {

        List<Post> posts = postRepository.findAll();
        log.debug("Retrieved {} posts", posts.size());

        return posts;
    }
}
