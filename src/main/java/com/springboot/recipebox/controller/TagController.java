package com.springboot.recipebox.controller;

import com.springboot.recipebox.payload.RecipeDTO;
import com.springboot.recipebox.payload.ResponseMessageDTO;
import com.springboot.recipebox.service.RecipeService;
import com.springboot.recipebox.service.TagService;
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
 * REST Controller for Tag-related actions
 */
@Tag(name = "Tag Controller", description = "REST API for tag-related actions")
@RestController
public class TagController
{
    TagService tagService;
    RecipeService recipeService;

    public TagController(RecipeService recipeService, TagService tagService)
    {
        this.tagService = tagService;
        this.recipeService = recipeService;
    }

    // recipe and tags  --------------------------------------------------------------

    /**
     * Return user's list of recipes with given tag name
     * @param username
     *      username of the user
     * @param tagName
     *      name of tag
     * @return
     *      list of recipes by a user with the given tag name
     */
    @ApiOperation(value = "GET or return user's list of recipes with given tag name", response = RecipeDTO.class, responseContainer = "List", tags = "Tag Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @GetMapping(path = "/tag-management/users/{username}/recipes/tag-search")
    //@GetMapping(path = "/recipes/{username}/{tagName}")
    ResponseEntity<List<RecipeDTO>> getUserRecipeListWithTagName(@PathVariable
                                                                    @ApiParam(name = "username", value = "current user logged in username", example = "user123")
                                                                    String username,
                                                                 @RequestParam("tagName")
                                                                    @ApiParam(name = "tag name", value = "tag name to have a list retrieved of", example = "Dinner")
                                                                    String tagName)
    {
        List<RecipeDTO> recipeDTOList;

        try
        {
            recipeDTOList = recipeService.getUserRecipeListWithTagName(username, tagName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<>((recipeDTOList), HttpStatus.OK);
    }

    /**
     * Get all recipes from all users by tag
     * @param tagName
     *      name of tag
     * @return
     *      list of recipes with tag name
     */
    @ApiOperation(value = "GET all recipes from all users with given tag name", response = RecipeDTO.class, responseContainer = "List", tags = "Tag Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @GetMapping(path = "/tag-management/recipes/tag-search")
    //@GetMapping(path = "/recipes/{tagName}")
    ResponseEntity<List<RecipeDTO>> getAllRecipeWithTagName(@RequestParam("tagName")
                                                            @ApiParam(name = "tag name", value = "tag name to have a list retrieved of", example = "Dinner")
                                                            String tagName)
    {
        List<RecipeDTO> recipeDTOList;

        try
        {
            recipeDTOList = recipeService.getAllRecipeWithTagName(tagName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>((recipeDTOList), HttpStatus.OK);
    }

    /**
     * Removes a tag on a recipe
     * @param recipeID
     *      ID of a recipe
     * @param tagName
     *      name of tag
     * @return
     *      success or failure
     */
    @ApiOperation(value = "REMOVE tag on a recipe", response = String.class, tags = "Tag Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @DeleteMapping(path = "/tag-management/recipes/{recipeID}/tag")
    //@DeleteMapping(path = "/recipe/{recipeID}/tags/{tagName}/remove")
    ResponseEntity<ResponseMessageDTO> removeRecipeTag(@PathVariable
                                                            @ApiParam(name = "recipe id", value = "ID of the recipe to have a tag removed of", example = "10")
                                                            int recipeID,
                                                       @RequestParam("tagName")
                                                            @ApiParam(name = "tag name", value = "tag name to be removed from a recipe", example = "Dinner")
                                                            String tagName)
    {
        try
        {
            tagService.removeTagFromRecipe(recipeID, tagName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseMessageDTO("failure"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseMessageDTO("success"), HttpStatus.OK);
    }

    //-----------------------------------------------------------------------------

    // Tag entity related actions

    // get tag list of user

    /**
     * Get tags created by a user
     * @param username
     *      username of user
     * @return
     *      list of tags created by a user
     */
    @ApiOperation(value = "GET all recipes from all users with given tag name", response = String.class, responseContainer = "List", tags = "Tag Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @GetMapping(path = "/tag-management/users/{username}/tags")
    //@GetMapping(path = "/{username}/tags")
    ResponseEntity<List<String>> getTagListOfUser(@PathVariable
                                                  @ApiParam(name = "username", value = "username of the user whose tag list is to be retrieved", example = "user123")
                                                  String username)
    {
        List<String> tagList;

        try
        {
            tagList = tagService.getAllTagNamesOfUser(username);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>((tagList), HttpStatus.OK);
    }

    /**
     * Create a tag for a user
     * @param username
     *      username of a user
     * @param tagName
     *      name of a tag
     * @return
     *      success or failure
     */
    @ApiOperation(value = "CREATE tag for a user", response = String.class, tags = "Tag Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @PostMapping(path = "/tag-management/users/{username}/tag")
    //@PostMapping(path = "/{username}/tags/create")
    ResponseEntity<String> createTagForUser(@PathVariable
                                                @ApiParam(name = "username", value = "username of the user who creates a tag", example = "user123")
                                                String username,
                                            @RequestParam("tagName")
                                                @ApiParam(name = "tag name", value = "tag name to be created for a user", example = "Dinner")
                                                String tagName)
    {
        try
        {
            tagService.createTagForUser(username, tagName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    /**
     * Update tag name of a tag
     * @param username
     *      username of user
     * @param tagName
     *      old tag name
     * @param newTagName
     *      updated tag name
     * @return
     *      success or failure
     */
    @ApiOperation(value = "UPDATE tag name", response = String.class, tags = "Tag Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @PutMapping(path = "/tag-management/users/{username}/tag")
    //@PutMapping(path = "/{username}/tags/{tagName}/update")
    ResponseEntity<ResponseMessageDTO> updateUserTag(@PathVariable
                                                        @ApiParam(name = "username", value = "user's username that will have a tag updated", example = "user123")
                                                        String username,
                                                     @RequestParam("oldTagName")
                                                        @ApiParam(name = "previous tag name", value = "old tag name to be updated", example = "Dinner")
                                                        String tagName,
                                                     @RequestParam("newTagName")
                                                        @ApiParam(name = "new tag name", value = "updated tag name", example = "Lunch")
                                                        String newTagName)
    {
        try
        {
            tagService.updateUserTag(username, tagName, newTagName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseMessageDTO("failure"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ResponseMessageDTO("success"), HttpStatus.OK);
    }

    /**
     * Remove a tag from user's tag list
     * @param username
     *      username of user
     * @param tagName
     *      tag name of the tag to be deleted
     * @return
     *      success or failure
     */
    @ApiOperation(value = "Delete a tag from the user's tag list", response = String.class, tags = "Tag Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @DeleteMapping(path = "/tag-management/users/{username}/tag")
    //@DeleteMapping(path = "/{username}/tags/{tagName}/delete")
    ResponseEntity<String> deleteUserTag(@PathVariable
                                            @ApiParam(name = "username", value = "user's username that will have a tag deleted", example = "user123")
                                            String username,
                                         @RequestParam("tagName")
                                            @ApiParam(name = "tag name", value = "tag to be deleted from the user's tag list", example = "Dinner")
                                            String tagName)
    {
        try
        {
            tagService.deleteUserTag(username, tagName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}