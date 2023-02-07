package com.springboot.recipebox.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecipeNameDTO
{
    @ApiModelProperty(notes = "identifier of a recipe", name = "recipeID", required = true)
    int recipeID;

    @ApiModelProperty(notes = "name of a recipe", name = "recipeName", required = true)
    String recipeName;
}
