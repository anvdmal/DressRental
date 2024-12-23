package com.DressRental;

import com.DressRental.models.entities.*;
import com.DressRental.models.enums.UserRoles;
import com.DressRental.repository.impl.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Init implements CommandLineRunner {
    private final UserRepositoryImpl userRepository;
    private final DressSizeRepositoryImpl dressSizeRepository;
    private final DressCategoryRepositoryImpl dressCategoryRepository;
    private final StatusRepositoryImpl statusRepository;
    private final RoleRepositoryImpl roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String defaultPassword;

    public Init(UserRepositoryImpl userRepository, DressSizeRepositoryImpl dressSizeRepository, DressCategoryRepositoryImpl dressCategoryRepository, StatusRepositoryImpl statusRepository, RoleRepositoryImpl roleRepository, PasswordEncoder passwordEncoder, @Value("${app.default.password}") String defaultPassword) {
        this.userRepository = userRepository;
        this.dressSizeRepository = dressSizeRepository;
        this.dressCategoryRepository = dressCategoryRepository;
        this.statusRepository = statusRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultPassword = defaultPassword;
    }

    @Override
    public void run(String... args) throws Exception {
        initRoles();
        initUsers();
        initCategories();
        initSizes();
        initStatuses();
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

    private void initCategories() {
        if (dressCategoryRepository.count() == 0) {
            initDressCategory();
        }
    }

    private void initSizes() {
        if (dressSizeRepository.count() == 0) {
            initDressSize();
        }
    }

    private void initStatuses() {
        if (statusRepository.count() == 0) {
            initStatus();
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

    private void initDressSize() {
        List<String> sizes = List.of("XS", "S", "M", "L", "XL", "XXL");

        for (String sizeName : sizes) {
            dressSizeRepository.create(new DressSize(sizeName));
        }
    }

    private void initDressCategory() {
        List<String> categories = List.of("Свадебные платья", "Вечерние платья", "Выпускные платья", "Коктейльные платья", "Бальные платья",
                "Будуарные платья", "Платья для фотосессии", "Платья для беременных");

        for (String categoryName : categories) {
            dressCategoryRepository.create(new DressCategory(categoryName));
        }
    }

    private void initStatus() {
        List<String> statuses = List.of("Ожидает примерки", "Активно", "Завершено", "Отменено");

        for (String status : statuses) {
            statusRepository.create(new Status(status));
        }
    }
}
