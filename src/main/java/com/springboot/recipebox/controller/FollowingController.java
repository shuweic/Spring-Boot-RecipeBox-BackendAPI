package com.springboot.recipebox.controller;

import com.springboot.recipebox.payload.FollowingDTO;
import com.springboot.recipebox.payload.UserDTO;
import com.springboot.recipebox.service.FollowingService;
import com.springboot.recipebox.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "User's Following and Follower Controller", description = "Rest APIs related to User Followings Entity (show following and follower relationship)!")
@RestController
@RequestMapping("/api/")
public class FollowingController {
    private FollowingService followingService;
    private UserService userService;

    public FollowingController(FollowingService followingService, UserService userService)
    {
        this.followingService = followingService;
        this.userService = userService;
    }

    @PostMapping("/users/{current_username}/follow/{following_username}")
    public ResponseEntity<FollowingDTO> addFollowing(@PathVariable(value = "current_username")
                                                         @ApiParam(name = "current_username", value = "the user that is logged in (current user)", example = "users")
                                                         String currentUsername,
                                                     @PathVariable(value = "following_username")
                                                        @ApiParam(name = "following_username", value = "the user that current user followed (other user)", example = "users")
                                                        String followedUsername
    )
    {
        int currentUserID = userService.getUserIDbyUsername(currentUsername);
        int followedUserID = userService.getUserIDbyUsername(followedUsername);
        return new ResponseEntity<>(followingService.addFollowing(currentUserID, followedUserID), HttpStatus.CREATED);
    }

    @GetMapping("/users/{current_username}/followings") // current user's following list
    public List<UserDTO> getFollowingByUsername(@PathVariable(value = "current_username")
                                                    @ApiParam(name = "current_username", value = "the user that is logged in (current user)", example = "users")
                                                    String currentUsername)
    {
        int currentUserID = userService.getUserIDbyUsername(currentUsername);
        return followingService.getAllUsersFollowedByUserId(currentUserID);
    }

    @GetMapping("/users/{current_username}/followers") // current user's followers list
    public List<UserDTO> getFollowersByUsername(@PathVariable(value = "current_username")
                                                    @ApiParam(name = "current_username", value = "the user that is logged in (current user)", example = "users")
                                                    String currentUsername)
    {
        int currentUserID = userService.getUserIDbyUsername(currentUsername);
        return followingService.getAllFollowersOfUserId(currentUserID);
    }

    @DeleteMapping("/users/{current_username}/followings/{following_username}/unfollow")
    public ResponseEntity<String> unfollowUser(@PathVariable(value = "current_username")
                                                   @ApiParam(name = "current_username", value = "the user that is logged in (current user)", example = "users")
                                                   String currentUsername,
                                               @PathVariable(value = "following_username")
                                                   @ApiParam(name = "following_username", value = "the user that current user followed (other user)", example = "users")
                                                   String followingUsername)
    {
        try
        {
            int currentUserID = userService.getUserIDbyUsername(currentUsername);
            int followedUserID = userService.getUserIDbyUsername(followingUsername);

            followingService.unfollowUser(currentUserID, followedUserID);

        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Unfollow not successful", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Unfollow successfully", HttpStatus.OK);
    }

}
