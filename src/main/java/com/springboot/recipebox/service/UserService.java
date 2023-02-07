package com.springboot.recipebox.service;

import com.springboot.recipebox.entity.User;
import com.springboot.recipebox.payload.UserDTO;

public interface UserService {
    String createUser(UserDTO userDto);

    UserDTO updateUser(UserDTO userDto, String username);

    int login(UserDTO userDto);

    User getUserbyUsername(String username);

    int getUserIDbyUsername(String username);
}
