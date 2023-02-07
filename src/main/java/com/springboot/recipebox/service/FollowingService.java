package com.springboot.recipebox.service;

import com.springboot.recipebox.payload.FollowingDTO;
import com.springboot.recipebox.payload.UserDTO;

import java.util.List;

public interface
FollowingService {

    FollowingDTO addFollowing(int currentUserID, int followingUserID);

    List<UserDTO> getAllUsersFollowedByUserId(int user_id);

    List<UserDTO> getAllFollowersOfUserId(int user_id);

    void unfollowUser(int user_id, int followingId);
}
