package com.microservice.user.infrastructure.adapter.driven.persistence.repository;

import com.microservice.user.infrastructure.adapter.driven.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
