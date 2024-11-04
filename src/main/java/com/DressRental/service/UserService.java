package com.DressRental.service;

import com.DressRental.dto.UserDTO;
import com.DressRental.dto.UserNameDTO;
import com.DressRental.dto.UserPasswordDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void addUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO findUserById(UUID id);
    UserDTO findUserByEmail(String email);
    void updateUserName(UserNameDTO userNameDTO);
    void updateUserPassword(UserPasswordDTO userPasswordDTO);
}
