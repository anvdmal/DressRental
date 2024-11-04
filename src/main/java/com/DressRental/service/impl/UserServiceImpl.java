package com.DressRental.service.impl;

import com.DressRental.dto.UserDTO;
import com.DressRental.dto.UserNameDTO;
import com.DressRental.dto.UserPasswordDTO;
import com.DressRental.entity.Role;
import com.DressRental.entity.User;
import com.DressRental.exception.UserAlreadyExistsException;
import com.DressRental.exception.UserNotFoundException;
import com.DressRental.repository.impl.RoleRepositoryImpl;
import com.DressRental.repository.impl.UserRepositoryImpl;
import com.DressRental.service.UserService;
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

    public UserServiceImpl(UserRepositoryImpl userRepository, RoleRepositoryImpl roleRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addUser(UserDTO userDTO) {
        if (!userRepository.findByEmail(userDTO.getEmail()).isEmpty()) {
            throw new UserAlreadyExistsException();
        } else {

            User user = modelMapper.map(userDTO, User.class);
            User newUser = userRepository.create(user);

            Role clientRole = roleRepository.findByName("CLIENT")
                    .orElseThrow(() -> new RuntimeException("Role CLIENT not found"));

            user.setRoles(List.of(clientRole));
            userRepository.update(user);

            modelMapper.map(newUser, UserDTO.class);
        }
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
                .orElseThrow(UserNotFoundException::new);
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
        User updatedUser = userRepository.findByEmail(userNameDTO.getEmail())
                .orElseThrow(UserNotFoundException::new);

        updatedUser.setName(userNameDTO.getName());
        userRepository.update(updatedUser);

        modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public void updateUserPassword(UserPasswordDTO userPasswordDTO) {
        User updatedUser = userRepository.findByEmail(userPasswordDTO.getEmail())
                .orElseThrow(UserNotFoundException::new);

        updatedUser.setPassword(userPasswordDTO.getPassword());
        userRepository.update(updatedUser);

        modelMapper.map(updatedUser, UserDTO.class);
    }
}

