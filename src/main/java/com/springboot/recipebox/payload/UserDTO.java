package com.springboot.recipebox.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
// I use lombok dependency to avoid add getter, setter, toString, hashcode, equals.
public class UserDTO {
    @ApiModelProperty(notes = "the user's username", name = "username", required = true)
    private String username;
    @ApiModelProperty(notes = "the user's password", name = "password", required = true)
    private String password;
    @ApiModelProperty(notes = "the user's user_type", name = "user_type", required = true)
    private int user_type;
}
