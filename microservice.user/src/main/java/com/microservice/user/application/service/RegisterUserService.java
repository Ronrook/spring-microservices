package com.microservice.user.application.service;

import com.microservice.user.domain.exception.UserAlreadyExistsException;
import com.microservice.user.domain.model.Role;
import com.microservice.user.domain.model.User;
import com.microservice.user.domain.port.in.RegisterUserUseCase;
import com.microservice.user.domain.port.out.PasswordEncoderPort;
import com.microservice.user.domain.port.out.RoleRepositoryPort;
import com.microservice.user.domain.port.out.UserRepositoryPort;

public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final RoleRepositoryPort roleRepository;
    private final PasswordEncoderPort passwordEncoder;

    public RegisterUserService(
            UserRepositoryPort userRepository,
            RoleRepositoryPort roleRepository,
            PasswordEncoderPort passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {

        if (userRepository.existsByNickname(user.getNickname())) {
            throw new UserAlreadyExistsException();
        }

        Role defaultRole = roleRepository
                .findByName("USER")
                .orElseThrow(() ->
                        new IllegalStateException("Default role USER not found"));

        user.addRole(defaultRole);

        user.updatePassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }
}