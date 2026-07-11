package com.microservice.user.domain.port.out;


import com.microservice.user.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {


    User save(User user);


    Optional<User> findByNickname(String nickname);


    boolean existsByNickname(String nickname);
}