package com.springboot.recipebox.service;

import java.util.List;

/**
 * Pantry Service Interface
 */
public interface PantryService
{
    /**
     * Adds the specified ingredient name to the pantry list of the user (given by the username).
     * @param username
     *          The username of the user
     * @param ingredientName
     *          name of the ingredient to be added in the user's pantry list.
     */
    void addIngredientToUserPantry(String username, String ingredientName);

    void removeIngredientFromUserPantry(String username, String ingredientName);

    List<String> getAllIngredientNamesByUsername(String username);
}
