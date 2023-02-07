package com.springboot.recipebox.service.impl;

import com.springboot.recipebox.entity.Following;
import com.springboot.recipebox.entity.User;
import com.springboot.recipebox.exception.ResourceNotFoundException;
import com.springboot.recipebox.helper.DTOHelper;
import com.springboot.recipebox.payload.FollowingDTO;
import com.springboot.recipebox.payload.UserDTO;
import com.springboot.recipebox.repository.FollowingRepository;
import com.springboot.recipebox.repository.UserRepository;
import com.springboot.recipebox.service.FollowingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowingServiceImpl implements FollowingService
{
    private FollowingRepository followingRepository;
    private UserRepository userRepository;

    public FollowingServiceImpl(FollowingRepository followingRepository, UserRepository userRepository) {
        this.followingRepository = followingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FollowingDTO addFollowing(int currentUserID, int followingUserID)
    {
        Following following = new Following();

        // retrieve current user by id
        User currentUser = RetrieveUserByUserId(currentUserID);
        following.setCurrentUser(currentUser);

        // retrieve user we are following by user id
        User followingUser = RetrieveUserByUserId(followingUserID);
        following.setFollowedUser(followingUser);

        Following newFollowing = followingRepository.save(following);

        return mapToDto(newFollowing);
    }

    public List<FollowingDTO> getFollowingByUserId(int user_id)
    {
        List<Following> followings = followingRepository.findFollowingByCurrentUserId(user_id);

        return followings.stream().map(following -> mapToDto(following)).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllUsersFollowedByUserId(int user_id)
    {
        List<UserDTO> usersFollowed = new ArrayList<>();
        List<Following> followings = followingRepository.findFollowingByCurrentUserId(user_id);
        for (Following f: followings
             ) {
            User followedUser = userRepository.findUserById(f.getFollowedUser().getId());
            usersFollowed.add(DTOHelper.mapUserToDto(followedUser));
        }

        return usersFollowed;
    }

    @Override
    public List<UserDTO> getAllFollowersOfUserId(int user_id)
    {
        List<Following> followings = followingRepository.getFollowersOfCurrentUserId(user_id);

        List<UserDTO> followers = new ArrayList<>();

        for (Following f: followings
        ) {
            User follower = userRepository.findUserById(f.getCurrentUser().getId());
            followers.add(DTOHelper.mapUserToDto(follower));
        }

        return followers;
    }

    @Override
    public void unfollowUser(int currentUserID, int followingUserID)
    {
        List<Following> followingRow = followingRepository.getFollowingFromCurrentUserIDAndFollowingUserID(currentUserID, followingUserID);
        followingRow.forEach(x -> followingRepository.delete(x));
    }

    private FollowingDTO mapToDto(Following following)
    {
        FollowingDTO followingDTO = new FollowingDTO();

        // user we're following
        followingDTO.setFollowedUser(DTOHelper.mapUserToDto(following.getFollowedUser()));

        // current user
        followingDTO.setCurrentUser(DTOHelper.mapUserToDto(following.getCurrentUser()));

        return followingDTO;
    }

    private Following mapToEntity(FollowingDTO followingDTO)
    {
        Following following = new Following();

        following.setFollowedUser(DTOHelper.mapDtoToUser(followingDTO.getFollowedUser()));
        following.setCurrentUser(DTOHelper.mapDtoToUser(followingDTO.getCurrentUser()));

        return following;
    }

    private User RetrieveUserByUserId(int user_id) {
        return userRepository.findById(user_id).orElseThrow(
                () -> new RuntimeException("user not found!"));
    }


}
