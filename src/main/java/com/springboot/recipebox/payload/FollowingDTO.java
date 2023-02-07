package com.springboot.recipebox.payload;

import com.springboot.recipebox.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FollowingDTO
{
    @ApiModelProperty(notes = "the user that current wanna follow or followed(other user)", name = "followedUser", required = true)
    private UserDTO followedUser; // user we are following
    @ApiModelProperty(notes = "the user that current logged in (current user)", name = "currentUser", required = true)
    private UserDTO currentUser; // current user
}