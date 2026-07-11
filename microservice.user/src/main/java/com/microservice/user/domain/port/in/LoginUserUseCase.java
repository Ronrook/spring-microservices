package com.microservice.user.domain.port.in;

public interface LoginUserUseCase {

    String login(String nickname, String password);

}