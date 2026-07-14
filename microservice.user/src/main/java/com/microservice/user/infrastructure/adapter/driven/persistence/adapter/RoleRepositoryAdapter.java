package com.microservice.user.infrastructure.adapter.driven.persistence.adapter;

import com.microservice.user.domain.model.Role;
import com.microservice.user.domain.port.out.RoleRepositoryPort;
import com.microservice.user.infrastructure.adapter.driven.persistence.repository.RoleJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final RoleJpaRepository roleRepository;

    public RoleRepositoryAdapter(RoleJpaRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name)
                .map(entity -> new Role(entity.getId(), entity.getName()));
    }
}
