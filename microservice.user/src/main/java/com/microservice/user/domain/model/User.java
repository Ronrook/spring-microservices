package com.microservice.user.domain.model;


import lombok.Getter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Getter
public class User {

    private Long id;
    private String name;
    private String lastname;
    private LocalDate birthdate;
    private String nickname;
    private String password;
    private Set<Role> roles;


    public User(
            String name,
            String lastname,
            LocalDate birthdate,
            String nickname,
            String password
    ){

        validateBirthdate(birthdate);

        this.name = name;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.nickname = nickname;
        this.password = password;
        this.roles = new HashSet<>();
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }




    private void validateBirthdate(LocalDate birthdate){

        if(birthdate == null){
            throw new IllegalArgumentException(
                    "Birthdate is required"
            );
        }
    }
}