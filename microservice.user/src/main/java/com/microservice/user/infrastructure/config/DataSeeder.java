package com.microservice.user.infrastructure.config;

import com.microservice.user.infrastructure.adapter.driven.persistence.entity.RoleEntity;
import com.microservice.user.infrastructure.adapter.driven.persistence.entity.UserEntity;
import com.microservice.user.infrastructure.adapter.driven.persistence.repository.RoleJpaRepository;
import com.microservice.user.infrastructure.adapter.driven.persistence.repository.UserJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final RoleJpaRepository roleRepository;
    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(RoleJpaRepository roleRepository,
                      UserJpaRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedRoles();
        seedUsers();
    }

    private void seedRoles() {
        if (roleRepository.findByName("USER").isEmpty()) {
            RoleEntity userRole = new RoleEntity();
            userRole.setName("USER");
            roleRepository.save(userRole);
            log.info(">>> Default USER role created");
        }
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            RoleEntity adminRole = new RoleEntity();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
            log.info(">>> Default ADMIN role created");
        }
    }

    private void seedUsers() {
        if (userRepository.count() > 0) {
            return;
        }

        RoleEntity userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("USER role not found"));

        createUser("Carlos", "Garcia", LocalDate.of(1990, 3, 15),
                "carlos", "password123", Set.of(userRole));

        createUser("Maria", "Lopez", LocalDate.of(1985, 7, 22),
                "maria", "password123", Set.of(userRole));

        createUser("Andres", "Martinez", LocalDate.of(1992, 11, 8),
                "andres", "password123", Set.of(userRole));

        createUser("Laura", "Rodriguez", LocalDate.of(1998, 1, 30),
                "laura", "password123", Set.of(userRole));

        log.info(">>> 4 test users seeded successfully");
    }

    private void createUser(String name, String lastname, LocalDate birthdate,
                            String nickname, String password, Set<RoleEntity> roles) {
        UserEntity user = new UserEntity();
        user.setName(name);
        user.setLastname(lastname);
        user.setBirthdate(birthdate);
        user.setNickname(nickname);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        userRepository.save(user);
    }
}
