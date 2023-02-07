package com.springboot.recipebox.service;

import com.springboot.recipebox.payload.RatingDTO;

import java.util.List;

public interface RatingService
{
    RatingDTO addRatingToRecipe(int recipeID, int score, String username);

    List<RatingDTO> getRatingListOfRecipe(int recipeID);

    RatingDTO getRatingByRecipeIdAndRatingId(int recipeID, int ratingID);

    RatingDTO updateRecipeRatingByUser(int recipeID, int ratingID, RatingDTO updatedRatingInput);

    void removeUserRatingOnRecipe(int recipeId, int ratingId);
}
