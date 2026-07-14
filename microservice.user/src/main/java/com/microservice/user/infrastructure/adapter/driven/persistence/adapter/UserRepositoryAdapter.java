package com.microservice.user.infrastructure.adapter.driven.persistence.adapter;

import com.microservice.user.domain.model.User;
import com.microservice.user.domain.port.out.UserRepositoryPort;
import com.microservice.user.infrastructure.adapter.driven.persistence.entity.UserEntity;
import com.microservice.user.infrastructure.adapter.driven.persistence.mapper.UserMapper;
import com.microservice.user.infrastructure.adapter.driven.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userRepository;
    private final UserMapper userMapper;

    public UserRepositoryAdapter(UserJpaRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity saved = userRepository.save(entity);
        return userMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .map(userMapper::toDomain);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
