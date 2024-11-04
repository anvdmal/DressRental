package com.DressRental.commandlinerunner;

import com.DressRental.dto.UserDTO;
import com.DressRental.dto.UserNameDTO;
import com.DressRental.dto.UserPasswordDTO;
import com.DressRental.entity.Role;
import com.DressRental.entity.User;
import com.DressRental.exception.InvalidDataException;
import com.DressRental.repository.impl.RoleRepositoryImpl;
import com.DressRental.repository.impl.UserRepositoryImpl;
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

    public ApplicationCommandLineRunner(RoleRepositoryImpl roleRepository, UserRepositoryImpl userRepository, UserServiceImpl userService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
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
                    3 - Поменять имя пользователя
                    4 - Поменять пароль пользователя""");

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
                default:
                    System.out.println("Неверная команда");
            }
            System.out.println("--------------------------------------");
        }
    }

    private void initRoles() {
        if (roleRepository.findByName("ADMIN").isPresent() && roleRepository.findByName("CLIENT").isPresent()) {
            System.out.println("Роли поинициализированы!");
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

    private void addUser() throws IOException { //ошибка добавления
        System.out.println("Введите данные нового пользователя в формате: email пароль имя");
        String[] userParams = bufferedReader.readLine().split("\\s+");

        UserDTO userDTO = new UserDTO(userParams[0], userParams[1], userParams[2]);

        try {
            this.userService.addUser(userDTO);
            System.out.println("Пользователь успешно добавлен!");
        } catch (InvalidDataException e) {
            System.out.println("Ошибка добавления пользователя: " + e.getMessage());
        }
    }

    private void viewAllUsers() {
        List<UserDTO> allUsers = this.userService.getAllUsers();
        allUsers.forEach(user -> System.out.printf("Пользователь: %s, email: %s%n", user.getName(), user.getEmail()));
    }


    private void updateUserName() throws IOException { //ошибка обновления
        System.out.println("Введите email и новое имя через пробел");
        String[] userParams = bufferedReader.readLine().split("\\s+");

        UserNameDTO newUserNameDTO = new UserNameDTO(userParams[0],userParams[1]);
        this.userService.updateUserName(newUserNameDTO);
        System.out.println("Имя пользователя успешно обновлено!");
    }

    private void updateUserPassword() throws IOException { //ошибка обновления
        System.out.println("Введите email и новый пароль через пробел");
        String[] userParams = bufferedReader.readLine().split("\\s+");

        UserPasswordDTO newUserPasswordDTO = new UserPasswordDTO(userParams[0], userParams[1]);
        this.userService.updateUserPassword(newUserPasswordDTO);
        System.out.println("Пароль успешно обновлен!");
    }
}



