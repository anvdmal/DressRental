package com.DressRental;

import com.DressRental.models.entities.Role;
import com.DressRental.models.entities.User;
import com.DressRental.models.enums.UserRoles;
import com.DressRental.repository.impl.RoleRepositoryImpl;
import com.DressRental.repository.impl.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Init implements CommandLineRunner {
    private final UserRepositoryImpl userRepository;
    private final RoleRepositoryImpl roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String defaultPassword;

    public Init(UserRepositoryImpl userRepository, RoleRepositoryImpl roleRepository, PasswordEncoder passwordEncoder, @Value("${app.default.password}") String defaultPassword) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultPassword = defaultPassword;
    }

    @Override
    public void run(String... args) throws Exception {
        initRoles();
        initUsers();
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            var adminRole = new Role(UserRoles.ADMIN);
            var clientRole = new Role(UserRoles.CLIENT);
            roleRepository.create(adminRole);
            roleRepository.create(clientRole);
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            initAdmin();
            initClient();
        }
    }

    private void initAdmin() {
        var adminRole = roleRepository.
                findRoleByName(UserRoles.ADMIN).orElseThrow();

        var adminUser = new User("admin@example.com", passwordEncoder.encode(defaultPassword), "Admin");
        adminUser.setRoles(List.of(adminRole));

        userRepository.create(adminUser);
    }

    private void initClient() {
        var userRole = roleRepository.
                findRoleByName(UserRoles.CLIENT).orElseThrow();

        var clientUser = new User("user@example.com", passwordEncoder.encode(defaultPassword), "User");
        clientUser.setRoles(List.of(userRole));

        userRepository.create(clientUser);
    }
}
