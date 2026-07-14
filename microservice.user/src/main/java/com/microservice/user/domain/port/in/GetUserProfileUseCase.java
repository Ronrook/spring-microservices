package com.microservice.user.domain.port.in;

import com.microservice.user.domain.model.User;

public interface GetUserProfileUseCase {

    User getProfile(String nickname);
}
