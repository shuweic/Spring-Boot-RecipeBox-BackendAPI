package com.springboot.recipebox.service;

import com.springboot.recipebox.entity.Recipe;
import com.springboot.recipebox.entity.Tags;
import com.springboot.recipebox.payload.RecipeDTO;

import java.util.List;

public interface TagService
{
    List<String> getTagNamesOfRecipe(int recipeID);

    void addTagsToRecipe(List<String> tagNames, Recipe recipe);

    void addTagToRecipe(String tagName, Recipe recipe);

    List<Integer> getUserRecipeIDWithTagName(String tagName, String username);

    List<Integer> getAllRecipeIDWithTagName(String tagName);

    Tags createTagForUser(String username, String tagName);

    List<String> getAllTagNamesOfUser(String username);

    void updateUserTag(String username, String tagName, String newTagName);

    void deleteUserTag(String username, String tagName);

    void removeTagFromRecipe(int recipeID, String tagName);
}
