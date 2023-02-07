package com.springboot.recipebox.controller;

import com.springboot.recipebox.payload.RatingDTO;
import com.springboot.recipebox.service.RatingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Rating-related actions
 */
@Tag(name = "Rating Controller", description = "REST API for rating-related actions")
@RestController
public class RatingController
{
    private final RatingService ratingService;

    public RatingController(RatingService ratingService)
    {
        this.ratingService = ratingService;
    }

    /**
     * Method to allow user to rate a recipe and store information about that rating
     * @param username
     *      username of the user who is rating a recipe
     * @param recipeId
     *      recipe ID of a recipe
     * @param score
     *      score that a user has given to a recipe when they rated it
     * @return
     *      success or failure response code
     */
    @ApiOperation(value = "POST or add a rating by a user to a recipe", tags = "Rating Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @PostMapping("/rating-management/recipes/{recipeId}/rate")
    //@PostMapping("{username}/recipes/{recipeId}/rate")
    public ResponseEntity<String> addUserRatingToRecipe(@RequestParam("username")
                                                            @ApiParam(name = "username", value = "username of the user who rates the recipe", example = "user123")
                                                            String username,
                                                        @PathVariable(value = "recipeId")
                                                            @ApiParam(name = "recipe ID", value = "ID of the recipe to be rated", example = "10")
                                                            int recipeId,
                                                        @RequestParam("score")
                                                            @ApiParam(name = "score", value = "score of the recipe to be rated", example = "4")
                                                            int score)
    {
        try
        {
            ratingService.addRatingToRecipe(recipeId, score, username);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    /**
     * Return list of ratings by users on a recipe
     * @param recipeId
     *      recipe ID of a recipe
     * @return
     *      list of ratings  by users on a recipe
     */
    @ApiOperation(value = "GET list of ratings by users on a recipe", response = RatingDTO.class, responseContainer = "List", tags = "Rating Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @GetMapping("/rating-management/recipes/{recipeId}/ratings")
    //@GetMapping("/recipes/{recipeId}/ratings")
    public ResponseEntity<List<RatingDTO>> getRatingListOfRecipe(@PathVariable(value = "recipeId")
                                                                     @ApiParam(name = "recipe ID", value = "ID of the recipe", example = "10")
                                                                     int recipeId)
    {
        List<RatingDTO> ratingDTO;

        try
        {
            ratingDTO = ratingService.getRatingListOfRecipe(recipeId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(ratingDTO, HttpStatus.OK);
    }

    /**
     * Returns a specific rating on a recipe based on rating id and recipe id
     * @param recipeId
     *      ID of recipe
     * @param ratingId
     *      ID of a rating on a specific recipe
     * @return
     *      Rating object that contains information about a rating
     */
    @ApiOperation(value = "GET specific rating on a recipe", response = RatingDTO.class, responseContainer = "List", tags = "Rating Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @GetMapping("/rating-management/recipes/{recipeId}/ratings/{ratingId}")
    public ResponseEntity<RatingDTO> getRatingOfRecipeWithRatingId(@PathVariable(value = "recipeId")
                                                                       @ApiParam(name = "recipe ID", value = "ID of the recipe", example = "10")
                                                                       int recipeId,
                                                                   @PathVariable(value = "ratingId")
                                                                        @ApiParam(name = "rating ID", value = "ID of the rating", example = "2")
                                                                        int ratingId)
    {
        RatingDTO ratingDTO;

        try
        {
            ratingDTO = ratingService.getRatingByRecipeIdAndRatingId(recipeId, ratingId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(ratingDTO, HttpStatus.OK);
    }

    /**
     * Update score of a specific rating on a recipe
     * @param recipeID
     *      ID of recipe
     * @param ratingID
     *      ID of rating to be updated
     * @param updatedScore
     *      new score of the rating
     * @return
     *      response code
     */
    @ApiOperation(value = "UPDATE score of a specific rating on a recipe", response = String.class, tags = "Rating Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @PutMapping("/rating-management/recipes/{recipeId}/ratings/{ratingId}")
    public ResponseEntity<String> updateRatingOfRecipe(@PathVariable(value = "recipeId")
                                                           @ApiParam(name = "recipe ID", value = "ID of the recipe", example = "10")
                                                           int recipeID,
                                                       @PathVariable(value = "ratingId")
                                                            @ApiParam(name = "rating ID", value = "ID of the recipe", example = "3")
                                                            int ratingID,
                                                       @RequestParam("score")
                                                           @ApiParam(name = "score", value = "updated rating score", example = "5")
                                                           int updatedScore)
    {
        try
        {
            RatingDTO ratingDTO = new RatingDTO();
            ratingDTO.setScore(updatedScore); // we only care about score here
           ratingService.updateRecipeRatingByUser(recipeID, ratingID, ratingDTO);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * Delete or remove a rating on a recipe
     * @param recipeID
     *      ID of recipe
     * @param ratingID
     *      ID of rating to be deleted
     * @return
     *      response code
     */
    @ApiOperation(value = "DELETE a specific rating on a recipe", response = String.class, tags = "Rating Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @DeleteMapping("/rating-management/recipes/{recipeId}/ratings/{ratingId}")
    public ResponseEntity<String> deleteRecipeRating(@PathVariable(value = "recipeId")
                                                         @ApiParam(name = "recipe ID", value = "ID of the recipe", example = "10")
                                                         int recipeID,
                                                     @PathVariable(value = "ratingId")
                                                         @ApiParam(name = "rating ID", value = "ID of the rating", example = "10")
                                                         int ratingID)
    {
        try
        {
            ratingService.removeUserRatingOnRecipe(recipeID, ratingID);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
