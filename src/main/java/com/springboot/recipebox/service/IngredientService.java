package com.springboot.recipebox.service;

import com.springboot.recipebox.payload.IngredientDTO;

import java.util.List;

public interface IngredientService
{
    void addIngredientListToRecipe(List<IngredientDTO> ingredientList, int RecipeID);

//    void addIngredient(String ingredientName, int recipeID);
//
//    void deleteIngredient(String ingredientName, int recipeID);
}
