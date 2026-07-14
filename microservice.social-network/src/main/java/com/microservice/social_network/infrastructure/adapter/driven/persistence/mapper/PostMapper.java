package com.microservice.social_network.infrastructure.adapter.driven.persistence.mapper;

import com.microservice.social_network.domain.model.Post;
import com.microservice.social_network.infrastructure.adapter.driven.persistence.entity.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "likes", ignore = true)
    PostEntity toEntity(Post post);

    Post toDomain(PostEntity entity);
}
