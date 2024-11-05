package com.DressRental.commandlinerunner;

import com.DressRental.dto.*;
import com.DressRental.entity.Role;
import com.DressRental.entity.User;
import com.DressRental.exception.InvalidDataException;
import com.DressRental.repository.impl.RoleRepositoryImpl;
import com.DressRental.repository.impl.UserRepositoryImpl;
import com.DressRental.service.impl.UserRatingServiceImpl;
import com.DressRental.service.impl.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {
    private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private final RoleRepositoryImpl roleRepository;
    private final UserRepositoryImpl userRepository;
    private final UserServiceImpl userService;
    private final UserRatingServiceImpl userRatingService;

    public ApplicationCommandLineRunner(RoleRepositoryImpl roleRepository,
                                        UserRepositoryImpl userRepository,
                                        UserServiceImpl userService,
                                        UserRatingServiceImpl userRatingService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.userRatingService = userRatingService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Сервис проката платьев!");
        initRoles();
        initAdmin();

        while (true) {
            System.out.println("""
                    \nВыберите действие:
                    1 - Добавить пользователя
                    2 - Посмотреть всех пользователей
                    3 - Изменить имя пользователя
                    4 - Изменить пароль пользователя
                    5 - Добавить рейтинг клиенту
                    6 - Посмотреть все оценки пользователя по email""");

            String input = bufferedReader.readLine();

            switch (input) {
                case "1":
                    this.addUser();
                    break;
                case "2":
                    this.viewAllUsers();
                    break;
                case "3":
                    this.updateUserName();
                    break;
                case "4":
                    this.updateUserPassword();
                    break;
                case "5":
                    this.addUserRating();
                    break;
                case "6":
                    this.getRatingsByUserEmail();
                    break;
                default:
                    System.out.println("Неверная команда");
            }
            System.out.println("--------------------------------------");
        }
    }

    private void initRoles() {
        if (roleRepository.findByName("ADMIN").isPresent() && roleRepository.findByName("CLIENT").isPresent()) {
            System.out.println("Роли проинициализированы!");
            return;
        }

        if (roleRepository.findByName("ADMIN").isEmpty()) {
            roleRepository.create(new Role("ADMIN"));
        }
        if (roleRepository.findByName("CLIENT").isEmpty()) {
            roleRepository.create(new Role("CLIENT"));
        }
    }

    private void initAdmin() {
        if (userRepository.findByEmail("admin@mail.ru").isPresent()) {
            System.out.println("Администратор уже существует!");
            return;
        }

        Role role = roleRepository.findByName("ADMIN").orElseThrow();
        User adminUser = new User("admin@mail.ru", "567", "Admin");

        adminUser.setRoles(List.of(role));
        userRepository.create(adminUser);
        System.out.println("Администратор создан!");
    }

    private void addUser() throws IOException {
        System.out.println("Введите данные нового пользователя в формате: email пароль имя");
        String[] userParams = bufferedReader.readLine().split("\\s+");

        UserDTO userDTO = new UserDTO(userParams[0], userParams[1], userParams[2]);

        try {
            this.userService.addUser(userDTO);
            System.out.println("Пользователь успешно добавлен!");
        } catch (InvalidDataException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void viewAllUsers() {
        List<UserDTO> allUsers = this.userService.getAllUsers();
        allUsers.forEach(user -> System.out.printf("Пользователь: %s, email: %s%n", user.getName(), user.getEmail()));
    }


    private void updateUserName() throws IOException {
        System.out.println("Введите email и новое имя через пробел");
        String[] userParams = bufferedReader.readLine().split("\\s+");

        UserNameDTO newUserNameDTO = new UserNameDTO(userParams[0], userParams[1]);

        try {
            this.userService.updateUserName(newUserNameDTO);
            System.out.println("Имя пользователя успешно обновлено!");
        } catch (InvalidDataException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void updateUserPassword() throws IOException {
        System.out.println("Введите email и новый пароль через пробел");
        String[] userParams = bufferedReader.readLine().split("\\s+");

        UserPasswordDTO newUserPasswordDTO = new UserPasswordDTO(userParams[0], userParams[1]);

        try {
            this.userService.updateUserPassword(newUserPasswordDTO);
            System.out.println("Пароль успешно обновлен!");
        } catch (InvalidDataException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void addUserRating() throws IOException {
        System.out.println("Введите email пользователя, рейтинг и комментарий через точку с запятой");
        String[] userRatingParams = bufferedReader.readLine().split("; ");

        UserRatingDTO newUserRatingDTO = new UserRatingDTO(userRatingParams[0], Integer.parseInt(userRatingParams[1]), userRatingParams[2]);

        try {
            this.userRatingService.addRating(newUserRatingDTO);
            System.out.println("Рейтинг успешно добавлен!");
        } catch (InvalidDataException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void getRatingsByUserEmail() throws IOException {
        System.out.println("Введите email");
        UserEmailDTO userEmailDTO = new UserEmailDTO(bufferedReader.readLine());

        try {
            List<UserRatingDTO> ratings = this.userRatingService.findRatingsByUserEmail(userEmailDTO);
            ratings.forEach(rating -> System.out.printf("Email: %s, рейтинг: %d, комментарий: \"%s\", дата: %s%n",
                    rating.getUserEmail(), rating.getRating(), rating.getComment(), rating.getReviewDate()));
        } catch (InvalidDataException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}