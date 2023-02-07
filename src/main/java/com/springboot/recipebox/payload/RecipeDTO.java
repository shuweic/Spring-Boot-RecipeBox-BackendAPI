package com.springboot.recipebox.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Data
@Getter
@Setter
public class RecipeDTO
{
    @ApiModelProperty(notes = "identifier of a recipe", name = "id", required = true)
    private int id;

    @ApiModelProperty(notes = "title of a recipe", name = "title", required = true)
    private String title;

    @ApiModelProperty(notes = "summary of a recipe", name = "summary", required = true)
    private String summary;

    @ApiModelProperty(notes = "cooking minutes of a recipe", name = "cookingMinutes", required = true)
    private int cookingMinutes;

    @ApiModelProperty(notes = "amount of servings a recipe has", name = "servings", required = true)
    private int servings;

    @ApiModelProperty(notes = "instructions list of a recipe", name = "instructionsList", required = true)
    private List<String> instructionsList;

    @ApiModelProperty(notes = "ingredients list of a recipe", name = "ingredientList", required = true)
    private List<IngredientDTO> ingredientList;

    @ApiModelProperty(notes = "tags list of a recipe", name = "tags", required = true)
    private List<String> tags;

    // Image is in form of URL
    // will need to figure out how to return image in this case
}
