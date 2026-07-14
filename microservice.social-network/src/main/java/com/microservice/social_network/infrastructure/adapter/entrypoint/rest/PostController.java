package com.microservice.social_network.infrastructure.adapter.entrypoint.rest;

import com.microservice.social_network.domain.port.in.CreatePostUseCase;
import com.microservice.social_network.domain.port.in.GetPostsUseCase;
import com.microservice.social_network.infrastructure.adapter.entrypoint.dto.PostRequest;
import com.microservice.social_network.infrastructure.adapter.entrypoint.dto.PostResponse;
import com.microservice.social_network.infrastructure.adapter.entrypoint.mapper.PostMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Posts", description = "Post management")
public class PostController {

    private final CreatePostUseCase createPostUseCase;
    private final GetPostsUseCase getPostsUseCase;
    private final PostMapper postMapper;

    public PostController(CreatePostUseCase createPostUseCase, GetPostsUseCase getPostsUseCase, PostMapper postMapper) {
        this.createPostUseCase = createPostUseCase;
        this.getPostsUseCase = getPostsUseCase;
        this.postMapper = postMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new post")
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostRequest request) {
        var post = postMapper.toDomain(request);
        var created = createPostUseCase.create(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.toResponse(created));
    }

    @GetMapping
    @Operation(summary = "Get all posts")
    public ResponseEntity<List<PostResponse>> findAll() {
        var posts = getPostsUseCase.findAll();
        var response = posts.stream()
                .map(postMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}
