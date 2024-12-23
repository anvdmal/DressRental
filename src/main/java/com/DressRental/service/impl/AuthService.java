package com.DressRental.service.impl;

import com.DressRental.dto.input.UserSignUpDTO;
import com.DressRental.exceptions.PasswordMatchingException;
import com.DressRental.exceptions.UserAlreadyExistsException;
import com.DressRental.models.entities.User;
import com.DressRental.models.enums.UserRoles;
import com.DressRental.exceptions.InvalidDataException;
import com.DressRental.repository.impl.RoleRepositoryImpl;
import com.DressRental.repository.impl.UserRepositoryImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private UserRepositoryImpl userRepository;
    private RoleRepositoryImpl roleRepository;
    private PasswordEncoder passwordEncoder;

    public AuthService(UserRepositoryImpl userRepository, RoleRepositoryImpl roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void register(UserSignUpDTO userSignUpDTO) {
        if (!userSignUpDTO.getPassword().equals(userSignUpDTO.getConfirmPassword())) {
            throw new PasswordMatchingException();
        }

        if (userRepository.findByEmail(userSignUpDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        var userRole = roleRepository.
                findRoleByName(UserRoles.CLIENT).orElseThrow(() -> new InvalidDataException("Роль не найдена!"));

        User user = new User(
                userSignUpDTO.getEmail(),
                passwordEncoder.encode(userSignUpDTO.getPassword()),
                userSignUpDTO.getName()
        );

        user.setRoles(List.of(userRole));

        this.userRepository.create(user);
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}

