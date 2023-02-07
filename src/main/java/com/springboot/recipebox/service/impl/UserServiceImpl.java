package com.springboot.recipebox.service.impl;

import com.springboot.recipebox.entity.User;
import com.springboot.recipebox.helper.DTOHelper;
import com.springboot.recipebox.payload.UserDTO;
import com.springboot.recipebox.repository.UserRepository;
import com.springboot.recipebox.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String createUser(UserDTO userDto) {
        String response;

        try
        {
            // convert DTO to entity
            User user = DTOHelper.mapDtoToUser(userDto);

            userRepository.save(user); // Note: only save user in DB and don't return new object
            response = "Sign up successful";
        }
        catch (Exception e)
        {
            response = "Sign up fail";
        }
        return response;
    }

    @Override
    public UserDTO updateUser(UserDTO userDto, String username) {
        // get user by username from the database
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new RuntimeException("user: "+ username + " not found!"));

        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());

        User updatedUser = userRepository.save(user);
        return DTOHelper.mapUserToDto(updatedUser);
    }

    @Override
    public int login(UserDTO userDto)
    {
        int loggedIn = -1;

        try
        {
            User user = getUserbyUsername(userDto.getUsername());

            if (userDto.getPassword().equals(user.getPassword()))
            {
                loggedIn =  user.getUser_type();
            }
        }
        catch (Exception e)
        {
        }

        return loggedIn;
    }

    @Override
    public User getUserbyUsername(String username)
    {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new RuntimeException("user: "+ username +" not found!"));
        return user;
    }

    @Override
    public int getUserIDbyUsername(String username)
    {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new RuntimeException("user: "+ username + " not found!"));
        return user.getId();
    }


}
