package com.microservice.user.application.service;

import com.microservice.user.domain.exception.InvalidCredentialsException;
import com.microservice.user.domain.exception.UserNotFoundException;
import com.microservice.user.domain.model.User;
import com.microservice.user.domain.port.in.LoginUserUseCase;
import com.microservice.user.domain.port.out.PasswordEncoderPort;
import com.microservice.user.domain.port.out.TokenProviderPort;
import com.microservice.user.domain.port.out.UserRepositoryPort;

public class LoginUserService implements LoginUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenProviderPort tokenProvider;

    public LoginUserService(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            TokenProviderPort tokenProvider) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public String login(String nickname, String password) {

        User user = userRepository.findByNickname(nickname)
                .orElseThrow();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return tokenProvider.generateToken(user.getNickname());
    }
}