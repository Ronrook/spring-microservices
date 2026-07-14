package com.microservice.social_network.infrastructure.adapter.driven.persistence.adapter;

import com.microservice.social_network.domain.model.Post;
import com.microservice.social_network.domain.port.out.PostRepositoryPort;
import com.microservice.social_network.infrastructure.adapter.driven.persistence.entity.PostEntity;
import com.microservice.social_network.infrastructure.adapter.driven.persistence.mapper.PostMapper;
import com.microservice.social_network.infrastructure.adapter.driven.persistence.repository.PostJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PostRepositoryAdapter implements PostRepositoryPort {

    private final PostJpaRepository postRepository;
    private final PostMapper postMapper;

    public PostRepositoryAdapter(PostJpaRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public Post save(Post post) {
        PostEntity entity = postMapper.toEntity(post);
        PostEntity saved = postRepository.save(entity);
        return postMapper.toDomain(saved);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id)
                .map(postMapper::toDomain);
    }
}
