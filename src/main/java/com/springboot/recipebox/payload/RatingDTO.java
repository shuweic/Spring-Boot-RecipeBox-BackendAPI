package com.springboot.recipebox.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RatingDTO
{
    @ApiModelProperty(notes = "unique identifier of a rating", name = "ratingID", required = true)
    private int ratingID;

    @ApiModelProperty(notes = "score of a rating", name = "score", required = true)
    private int score;

    @ApiModelProperty(notes = "ID of the recipe that has this rating", name = "recipeID", required = true)
    private int recipeID;

    @ApiModelProperty(notes = "username of the user who made this rating", name = "username", required = true)
    private String username; // of user who posted rating
}
