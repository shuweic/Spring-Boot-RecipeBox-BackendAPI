package com.springboot.recipebox.controller;

import com.springboot.recipebox.payload.LoginResponseDTO;
import com.springboot.recipebox.payload.ResponseMessageDTO;
import com.springboot.recipebox.payload.UserDTO;
import com.springboot.recipebox.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "User Controller", description = "Rest APIs related to User Entity")
@RestController
@RequestMapping("/user")
public class UserController
{
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // create User as signup features
    @PostMapping("/signup")
    public ResponseEntity<ResponseMessageDTO> createUser(@RequestBody UserDTO userDto)
    {
        try
        {
            userService.createUser(userDto);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(new ResponseMessageDTO("failure"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ResponseMessageDTO("success"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserDTO userDto)
    {
        LoginResponseDTO loginResponse;
        try
        {
            int userType = userService.login(userDto);

            if (userType > -1)
            {
                loginResponse = new LoginResponseDTO("success", userType);
            }
            else
            {
                loginResponse = new LoginResponseDTO("failure", -1);
            }

        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    // update user info by username api
    @PutMapping("/editAccount/{username}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDto, @PathVariable(name = "username") @ApiParam(name = "username", value = "the user that is logged in (current user)", example = "users")
     String username)
    {
        UserDTO updatedUser;
        try
        {
            updatedUser = userService.updateUser(userDto, username);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
