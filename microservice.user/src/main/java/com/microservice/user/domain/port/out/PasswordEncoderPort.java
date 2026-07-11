package com.microservice.user.domain.port.out;


public interface PasswordEncoderPort {

    String encode(String password);

    boolean matches(String rawPassword, String encodedPassword);

}