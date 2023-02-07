package com.springboot.recipebox.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PantryDTO
{
    @ApiModelProperty(notes = "name of the ingredient added in the user's pantry list", name = "name", required = true)
    String name;
}
