package com.microservice.social_network.infrastructure.adapter.driven.persistence.repository;

import com.microservice.social_network.infrastructure.adapter.driven.persistence.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {
}
