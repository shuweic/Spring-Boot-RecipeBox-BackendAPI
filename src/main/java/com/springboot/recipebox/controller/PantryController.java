package com.springboot.recipebox.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import com.springboot.recipebox.helper.APIHelper;
import com.springboot.recipebox.payload.PantryDTO;
import com.springboot.recipebox.service.PantryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

/**
 * REST Controller for Pantry-related actions
 */
@Tag(name = "Pantry Controller", description = "REST API for pantry-related actions")
@RestController
public class PantryController
{
    PantryService pantryService;

    /**
     * Constructor for dependency injection of pantry controller
     * @param pantryService
     *      Pantry service for this controller
     */
    public PantryController(PantryService pantryService)
    {
        this.pantryService = pantryService;
    }

    /**
     * Return list of ingredients that start with the given input using the SpoonacularAPI.
     * @param searchStr
     *      input string that will get passed in as a parameter in the SpoonacularAPI
     * @return
     *      list of ingredient names
     */
    @ApiOperation(value = "GET list of ingredient names that start with the input string", response = PantryDTO.class, responseContainer = "List", tags = "Pantry Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @GetMapping(path = "/ingredient-search/{searchStr}")
    ResponseEntity<List<PantryDTO>> searchIngredient(@PathVariable
                                                     @ApiParam(name = "ingredient name", value = "input string", example = "salt") String searchStr)
    {
        List<PantryDTO> pantryDTOList = new ArrayList<>();

        try
        {
            // RestTemplate allows server side to connect to API to retrieve data
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(APIHelper.getIngredientSearchURL(searchStr), String.class);

            ObjectMapper mapper = new ObjectMapper();


			JsonNode root = mapper.readTree(java.lang.String.valueOf(response.getBody()));

            if (root.isArray())
            {
                for (JsonNode node : root)
                {
                    // ingredient name
                    PantryDTO pantryDTO = new PantryDTO(node.path("name").asText());
                    pantryDTOList.add(pantryDTO);
                }
            }
		}
        catch (Exception e)
        {
			e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

        return new ResponseEntity<>(pantryDTOList, HttpStatus.OK);
    }

    /**
     * Returns list of ingredient names that the user has added in their pantry.
     * @param username
     *      username of the user of whose pantry list we're getting
     * @return
     *      list of ingredient names of the given username's pantry list
     */
    @ApiOperation(value = "GET list of ingredient names of the user's pantry list", response = PantryDTO.class, responseContainer = "List", tags = "Pantry Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @GetMapping(path = "/ingredient-management/users/{username}/ingredients")
    ResponseEntity<List<PantryDTO>> getAllIngredientsOfUser(@PathVariable
                                                            @ApiParam(name = "username", value = "username of the user", example = "user123") String username)
    {
        List<PantryDTO> pantryDTOList = new ArrayList<>();
        try
        {
            pantryService.getAllIngredientNamesByUsername(username).forEach(ingredientName -> pantryDTOList.add(new PantryDTO(ingredientName)));

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(pantryDTOList, HttpStatus.OK);
    }

    /**
     * Add the given ingredient name to the user's pantry list.
     * @param username
     *      user username
     * @param ingredientName
     *      name of ingredient
     * @return
     *      success or failure response
     */
    @ApiOperation(value = "POST or add the given ingredient name to the user's pantry list", response = String.class, tags = "Pantry Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @PostMapping(path = "/ingredient-management/users/{username}/ingredients")
    ResponseEntity<String> addIngredientToUser(@PathVariable String username,
                                               @RequestParam("name") String ingredientName)
    {
        try
        {
//            String username = pantryNode.path("username").asText();
//            String ingredientName = pantryNode.path("ingredientName").asText();

            pantryService.addIngredientToUserPantry(username, ingredientName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    /**
     * Remove the given ingredient name from the user's pantry list.
     * @param username
     *      user username
     * @param ingredientName
     *      name of ingredient
     * @return
     *      success or failure response
     */
    @ApiOperation(value = "DELETE or remove the given ingredient name from the user's pantry list", response = String.class, tags = "Pantry Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @DeleteMapping(path = "/ingredient-management/users/{username}/ingredients")
    ResponseEntity<String> deleteIngredientByUser(@PathVariable String username,
                                                  @RequestParam("name") String ingredientName)
    {
        try
        {
            pantryService.removeIngredientFromUserPantry(username, ingredientName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
