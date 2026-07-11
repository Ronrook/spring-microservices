package com.microservice.user.domain.port.out;

import com.microservice.user.domain.model.Role;

import java.util.Optional;

public interface RoleRepositoryPort {

    Optional<Role> findByName(String name);

}