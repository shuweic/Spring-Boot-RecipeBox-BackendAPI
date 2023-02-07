package com.springboot.recipebox.repository;

import com.springboot.recipebox.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer>
{
    List<Rating> getAllByRecipeId(int recipeID);

    Rating getRatingByIdAndAndRecipeId(int ratingID, int recipeID);
}
