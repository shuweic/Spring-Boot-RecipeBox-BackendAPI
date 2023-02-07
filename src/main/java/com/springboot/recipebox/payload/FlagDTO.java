package com.springboot.recipebox.payload;

import lombok.Data;

@Data
public class FlagDTO
{
    private String detail;
    private String title;
    private int recipeID;
}
