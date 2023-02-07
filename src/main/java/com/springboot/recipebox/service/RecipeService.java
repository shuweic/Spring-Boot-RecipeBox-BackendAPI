package com.springboot.recipebox.service;

import com.springboot.recipebox.entity.Instructions;
import com.springboot.recipebox.entity.Recipe;
import com.springboot.recipebox.payload.RecipeDTO;
import com.springboot.recipebox.payload.RecipeNameDTO;

import java.util.List;

public interface RecipeService
{
    void deleteUserRecipe(int recipeID);

    List<RecipeDTO> getAllRecipesCreatedByUser(String username);

    RecipeDTO getRecipeDTOByID(int recipeID);

    Recipe getRecipeByID(int recipeID);

    void addRecipeToUserSavedRecipeList(int recipeID, String username);

    List<RecipeNameDTO> getAllSavedRecipeNameAndIDsByUser(String username);

    // Demo 3
    int createUserBlankRecipe(String recipeName, String username);

    void updateRecipeGeneralInfo(Recipe newRecipe, int recipeID);

    void updateRecipeInstructions(List<Instructions> instructionsList, int recipeID);

    // tag-related methods
    void addTagsToRecipe(List<String> tagNames, int recipeID);

    List<RecipeDTO> getUserRecipeListWithTagName(String username, String tagName);

    List<RecipeDTO> getAllRecipeWithTagName(String tagName);
}
