package com.springboot.recipebox.service.impl;

import com.springboot.recipebox.entity.Rating;
import com.springboot.recipebox.payload.RatingDTO;
import com.springboot.recipebox.repository.RatingRepository;
import com.springboot.recipebox.repository.RecipeRepository;
import com.springboot.recipebox.repository.UserRepository;
import com.springboot.recipebox.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService
{
    RatingRepository ratingRepository;

    UserRepository userRepository;

    RecipeRepository recipeRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, UserRepository userRepository, RecipeRepository recipeRepository)
    {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public RatingDTO addRatingToRecipe(int recipeID, int score, String username)
    {
        // crate new rating object
        Rating rating = new Rating();
        rating.setScore(score);
        rating.setRecipe(recipeRepository.findRecipeById(recipeID));
        rating.setUser(userRepository.findUserByUsername(username).orElseThrow(() -> new RuntimeException("User invalid")));

        // save rating row in database
        ratingRepository.save(rating);

        return mapRatingToDTO(rating);
    }

    @Override
    public List<RatingDTO> getRatingListOfRecipe(int recipeID)
    {
        List<RatingDTO> ratingDTOList = new ArrayList<>();

        ratingRepository.getAllByRecipeId(recipeID)
                .forEach(rating -> ratingDTOList.add(mapRatingToDTO(rating)));

        return ratingDTOList;
    }

    @Override
    public RatingDTO getRatingByRecipeIdAndRatingId(int recipeID, int ratingID)
    {
        RatingDTO ratingDTO;
        ratingDTO = mapRatingToDTO(ratingRepository.getRatingByIdAndAndRecipeId(ratingID, recipeID));
        return ratingDTO;
    }

    @Override
    public RatingDTO updateRecipeRatingByUser(int recipeID, int ratingID, RatingDTO updatedRatingInput)
    {
        Rating rating = ratingRepository.getRatingByIdAndAndRecipeId(ratingID, recipeID);
        rating.setScore(updatedRatingInput.getScore()); // score is the only data chane we're concerned for now
        return mapRatingToDTO(ratingRepository.save(rating));
    }

    @Override
    public void removeUserRatingOnRecipe(int recipeID, int ratingID)
    {
        Rating rating = ratingRepository.getRatingByIdAndAndRecipeId(ratingID, recipeID);
        ratingRepository.delete(rating);
    }

    private static RatingDTO mapRatingToDTO(Rating rating)
    {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setScore(rating.getScore());
        ratingDTO.setUsername(rating.getUser().getUsername());
        ratingDTO.setRecipeID(rating.getRecipe().getId());
        ratingDTO.setRatingID(rating.getId());

        return ratingDTO;
    }
}