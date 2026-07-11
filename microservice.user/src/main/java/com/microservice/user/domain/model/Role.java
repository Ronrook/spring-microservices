package com.microservice.user.domain.model;

import lombok.Getter;

@Getter
public class Role {

    private Long id;
    private String name;


    public Role(Long id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }


    private void validateName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Role name cannot be empty"
            );
        }
    }
}