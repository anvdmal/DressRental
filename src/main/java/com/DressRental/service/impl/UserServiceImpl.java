package com.DressRental.service.impl;

import com.DressRental.dto.UserDTO;
import com.DressRental.dto.UserNameDTO;
import com.DressRental.dto.UserPasswordDTO;
import com.DressRental.entity.Role;
import com.DressRental.entity.User;
import com.DressRental.exception.InvalidDataException;
import com.DressRental.exception.UserAlreadyExistsException;
import com.DressRental.exception.UserNotFoundException;
import com.DressRental.repository.impl.RoleRepositoryImpl;
import com.DressRental.repository.impl.UserRepositoryImpl;
import com.DressRental.service.UserService;
import jakarta.validation.ConstraintViolation;
import com.DressRental.utils.ValidationUtilImpl;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepositoryImpl userRepository;
    private final RoleRepositoryImpl roleRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtilImpl validationUtil;

    public UserServiceImpl(UserRepositoryImpl userRepository, RoleRepositoryImpl roleRepository, ModelMapper modelMapper, ValidationUtilImpl validationUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void addUser(UserDTO userDTO) {
        validateUser(userDTO, "некорректные данные для добавления!");

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        User user = modelMapper.map(userDTO, User.class);
        User newUser = userRepository.create(user);

        Role clientRole = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new InvalidDataException("Роль не найдена!"));

        user.setRoles(List.of(clientRole));
        userRepository.update(user);

        modelMapper.map(newUser, UserDTO.class);

    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> allUsers = userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        if (allUsers.isEmpty()) {
            throw new UserNotFoundException();
        } else {
            return allUsers;
        }
    }

    @Override
    public UserDTO findUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void updateUserName(UserNameDTO userNameDTO) {
        validateUser(userNameDTO, "некорректные данные для обновления имени!");

        User updatedUser = userRepository.findByEmail(userNameDTO.getEmail())
                .orElseThrow(UserNotFoundException::new);

        updatedUser.setName(userNameDTO.getName());
        userRepository.update(updatedUser);

        modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public void updateUserPassword(UserPasswordDTO userPasswordDTO) {
        validateUser(userPasswordDTO, "некорректные данные для обновления пароля!");

        User updatedUser = userRepository.findByEmail(userPasswordDTO.getEmail())
                .orElseThrow(UserNotFoundException::new);

        updatedUser.setPassword(userPasswordDTO.getPassword());
        userRepository.update(updatedUser);

        modelMapper.map(updatedUser, UserDTO.class);
    }

    private <T> void validateUser(T userDTO, String exceptionMessage) {
        if (!this.validationUtil.isValid(userDTO)) {
            this.validationUtil
                    .violations(userDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            throw new InvalidDataException(exceptionMessage);
        }
    }
}
