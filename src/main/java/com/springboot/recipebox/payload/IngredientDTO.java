package com.springboot.recipebox.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDTO
{
    @ApiModelProperty(notes = "name of the ingredient added to a user recipe", name = "ingredientName", required = true)
    private String ingredientName;

    @ApiModelProperty(notes = "amount of the ingredient", name = "amount", required = true)
    private int amount;

    @ApiModelProperty(notes = "unit of measurement of the ingredient", name = "unit", required = true)
    private String unit;
}
