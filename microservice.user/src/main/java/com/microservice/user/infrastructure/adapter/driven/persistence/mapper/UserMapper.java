package com.microservice.user.infrastructure.adapter.driven.persistence.mapper;

import com.microservice.user.domain.model.Role;
import com.microservice.user.domain.model.User;
import com.microservice.user.infrastructure.adapter.driven.persistence.entity.RoleEntity;
import com.microservice.user.infrastructure.adapter.driven.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(User user);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    User toDomain(UserEntity entity);

    @Named("mapRoles")
    default Set<Role> mapRoles(Set<RoleEntity> roles) {
        if (roles == null) {
            return Collections.emptySet();
        }
        return roles.stream()
                .map(this::toRoleDomain)
                .collect(Collectors.toSet());
    }

    default Role toRoleDomain(RoleEntity entity) {
        return new Role(entity.getId(), entity.getName());
    }

    default RoleEntity toRoleEntity(Role role) {
        RoleEntity entity = new RoleEntity();
        entity.setId(role.getId());
        entity.setName(role.getName());
        return entity;
    }
}
