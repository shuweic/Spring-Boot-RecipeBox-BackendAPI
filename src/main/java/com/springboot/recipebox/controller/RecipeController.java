package com.springboot.recipebox.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.recipebox.entity.Instructions;
import com.springboot.recipebox.entity.Recipe;
import com.springboot.recipebox.helper.APIHelper;
import com.springboot.recipebox.payload.*;
import com.springboot.recipebox.service.IngredientService;
import com.springboot.recipebox.service.RecipeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * REST Controller for actions related to recipe creation and search
 */
@Tag(name = "Recipe Controller", description = "REST API for recipe entity related actions")
@RestController
public class RecipeController
{
    // This is path to which the file will be written
    // You can optionally store it the database if the files
    // are of the range of 1MB. Larger files slow down the
    // database query speeds over time
    //private static String uploadPath = "/uploadedImages/";

    HttpServletRequest httpServletRequest;

    RecipeService recipeService;

    IngredientService ingredientService;

    public RecipeController(RecipeService recipeService, IngredientService ingredientService,
                            HttpServletRequest httpServletRequest)
    {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * Search for random recipes using external API
     * @return
     *      List of recipes
     */
    @ApiOperation(value = "GET or return random recipes found from SpoonacularAPI", response = RecipeDTO.class, responseContainer = "List", tags = "Recipe Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @GetMapping(path = "/recipe-search/random")
    ResponseEntity<List<RecipeDTO>> randomRecipeSearch()
    {
        // RestTemplate allows server side to connect to API to retrieve data
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(APIHelper.RandomSearchURL, String.class);

        ObjectMapper mapper = new ObjectMapper();
        List<RecipeDTO> recipeList = new ArrayList<>();

        try
        {
            JsonNode root = mapper.readTree(java.lang.String.valueOf(response.getBody()));
            JsonNode results = root.path("recipes");

            if (results.isArray())
            {
                for (JsonNode node : results)
                {
                    recipeList.add(APIHelper.parseJsonNodeToRecipe(node));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(recipeList, HttpStatus.OK);
    }

    // SAVING RECIPES ----------------------

    /**
     *
     * @param username
     * @return
     */
    @ApiOperation(value = "GET or return recipe names & IDs created by a user", response = RecipeNameDTO.class, responseContainer = "List", tags = "Recipe Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @GetMapping(path = "/recipe-management/users/{username}/saved-recipe-names-and-ids")
    ResponseEntity<List<RecipeNameDTO>> getAllRecipeNamesAndIDsSavedRecipesByUser(@PathVariable
                                                                                  @ApiParam(name = "username", value = "username of the user who created the recipe", example = "user123")
                                                                                  String username)
    {
        List<RecipeNameDTO> recipeNameDTOList;

        try
        {
            recipeNameDTOList = recipeService.getAllSavedRecipeNameAndIDsByUser(username);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>((recipeNameDTOList), HttpStatus.OK);
    }

    /**
     *
     * @param recipeID
     * @param username
     * @return
     */
    @ApiOperation(value = "Saves given recipe to user's saved recipe list", response = String.class, tags = "Recipe Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @PostMapping(path = "/recipe-management/users/{username}/recipes/{recipeID}/save")
    ResponseEntity<String> addRecipeToUserSavedRecipes(@PathVariable int recipeID,
                                                       @PathVariable String username)
    {
        try
        {
            recipeService.addRecipeToUserSavedRecipeList(recipeID, username);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    // -------------------------------------

    /**
     * Return recipes created by a user
     * @param username
     *      username of a user
     * @return
     *      list of recipes created by a user
     */
    @ApiOperation(value = "GET or return recipes created by a user", response = RecipeDTO.class, responseContainer = "List", tags = "Recipe Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @GetMapping(path = "/recipe-management/users/{username}/recipes")
    ResponseEntity<List<RecipeDTO>> getAllRecipesCreatedByUser(@PathVariable
                                                               @ApiParam(name = "username", value = "username of the user who created the recipe", example = "user123")
                                                               String username)
    {
        List<RecipeDTO> recipeDTOList;

        try
        {
            recipeDTOList = recipeService.getAllRecipesCreatedByUser(username);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>((recipeDTOList), HttpStatus.OK);
    }

    /**
     * Return recipe names & IDs created by a user
     * @param username
     *      username of a user
     * @return
     *      list of recipes created by a user
     */
    @ApiOperation(value = "GET or return recipe names & IDs created by a user", response = RecipeNameDTO.class, responseContainer = "List", tags = "Recipe Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @GetMapping(path = "/recipe-management/users/{username}/recipe-names-and-ids")
    ResponseEntity<List<RecipeNameDTO>> getAllRecipeNamesAndIDsCreatedByUser(@PathVariable
                                                                             @ApiParam(name = "username", value = "username of the user who created the recipe", example = "user123")
                                                                             String username)
    {
        List<RecipeNameDTO> recipeNameDTOList = new ArrayList<>();

        try
        {
            recipeService.getAllRecipesCreatedByUser(username).forEach(
                    recipeDTO -> recipeNameDTOList.add(new RecipeNameDTO(recipeDTO.getId(), recipeDTO.getTitle())));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>((recipeNameDTOList), HttpStatus.OK);
    }

    /**
     * Returns a specific recipe created by a user
     * @param recipeID
     *      ID of a recipe
     * @return
     *      a recipe object
     */
    @ApiOperation(value = "GET or return recipe based from recipe ID", response = RecipeDTO.class, tags = "Recipe Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @GetMapping(path = "/recipe-management/recipes/{recipeID}")
    ResponseEntity<RecipeDTO> getRecipeByID(@PathVariable
                                            @ApiParam(name = "recipe ID", value = "ID of the recipe to be retrieved", example = "10")
                                            int recipeID)
    {
        RecipeDTO recipe;

        try
        {
            recipe = recipeService.getRecipeDTOByID(recipeID);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>((recipe), HttpStatus.OK);
    }

    /**
     * Creates a blank recipe object container
     * @param recipeName
     *      name of the recipe to be created
     * @param username
     *      username of the user creating the recipe
     * @return
     *      recipe ID of the created recipe
     */
    @ApiOperation(value = "Creates a blank recipe object container", response = String.class, tags = "Recipe Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @PostMapping(path = "/recipe-management/users/{username}/recipe")
    ResponseEntity<String> createBlankUserRecipe(@RequestParam("recipeName")
                                                    @ApiParam(name = "recipeName", value = "name of the recipe to be created", example = "Cake")
                                                    String recipeName,
                                                 @PathVariable
                                                    @ApiParam(name = "username", value = "username of the user who created the recipe", example = "user123")
                                                    String username)
    {
        int recipeID;

        try
        {
            recipeID = recipeService.createUserBlankRecipe(recipeName, username);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Return value success or return the recipe object created?
        return new ResponseEntity<>(Integer.toString(recipeID), HttpStatus.CREATED);
    }

    /**
     * Updates the general info of a recipe
     * @param recipeID
     *      ID of recipe to be updated
     * @param recipeName
     *      new recipe name
     * @param servings
     *      new servings amount
     * @param cookingTime
     *      new cooking time
     * @param summary
     *      new summary
     * @param tagsListStr
     *      new tags list in JSON list format
     * @return
     *      success or failure
     */
    @ApiOperation(value = "UPDATE general info of a recipe", response = String.class, tags = "Recipe Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @PutMapping(path = "/recipe-management/recipes/{recipeID}/update-general-info")
    public ResponseEntity<String> updateRecipeGeneralInfo(@PathVariable
                                                              @ApiParam(name = "recipe id", value = "id of the recipe to be updated", example = "10")
                                                              int recipeID,
                                                          //@RequestParam("image") MultipartFile image,
                                                          @RequestParam("recipeName")
                                                              @ApiParam(name = "recipe name", value = "name of the recipe", example = "Cake")
                                                              String recipeName,
                                                          @RequestParam("servings")
                                                              @ApiParam(name = "servings", value = "amount of servings that the recipe can make", example = "2")
                                                              String servings,
                                                          @RequestParam("cookingTime")
                                                              @ApiParam(name = "cooking time", value = "cooking minutes of the recipe", example = "20")
                                                              String cookingTime,
                                                          @RequestParam("summary")
                                                              @ApiParam(name = "summary", value = "summary of the recipe", example = "The most delicious cake ever")
                                                              String summary,
                                                          @RequestParam("tags")
                                                              @ApiParam(name = "tags", value = "list of tags the recipe has in JSON string list format",
                                                                      example = "[\"Korean\", \"Dessert\"]")
                                                              String tagsListStr)
    {
        try
        {
            Recipe recipe = new Recipe();

//            if (image != null)
//            {
//                byte[] file = image.getBytes();
//                SerialBlob blob = new SerialBlob(file);
//                Blob img = blob;
//                recipe.setImage(img);
//            }

            recipe.setTitle(recipeName);
            recipe.setServings(Integer.parseInt(servings));
            recipe.setCookingMinutes(Integer.parseInt(cookingTime));
            recipe.setSummary(summary);

            recipeService.updateRecipeGeneralInfo(recipe, recipeID);

            // update tags
            List<String> tagsList = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            JsonNode values = mapper.readTree(tagsListStr);

            if (values.isArray())
            {
                for (JsonNode node : values)
                {
                    tagsList.add(node.asText());
                }
            }

            recipeService.addTagsToRecipe(tagsList, recipeID);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * Updates ingredient list of a recipe
     * @param recipeID
     *      ID of recipe
     * @param ingredientsListStr
     *      updated list of ingredients
     * @return
     *      success or failure
     */
    @ApiOperation(value = "UPDATE ingredient list of a recipe", response = String.class, tags = "Recipe Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @PutMapping(path = "/recipe-management/recipes/{recipeID}/update-recipe-ingredients")
    public ResponseEntity<String> updateRecipeIngredients(@PathVariable
                                                              @ApiParam(name = "recipe id", value = "id of the recipe to be updated", example = "10")
                                                              int recipeID,
                                                          @RequestParam("ingredients")
                                                                @ApiParam(name = "ingredients list", value = "list of ingredients in JSON object array format",
                                                                        example = "[\n" +
                                                                                "    {\n" +
                                                                                "        \"name\": \"table salt\",\n" +
                                                                                "        \"amount\": 9,\n" +
                                                                                "        \"unit\": \"mg\"\n" +
                                                                                "    },\n" +
                                                                                "    {\n" +
                                                                                "        \"name\": \"salt pork\",\n" +
                                                                                "        \"amount\": 8,\n" +
                                                                                "        \"unit\": \"pieces\"\n" +
                                                                                "    },\n" +
                                                                                "    {\n" +
                                                                                "        \"name\": \"salt and pepper\",\n" +
                                                                                "        \"amount\": 9,\n" +
                                                                                "        \"unit\": \"g\"\n" +
                                                                                "    }\n" +
                                                                                "]")
                                                                String ingredientsListStr)
    {
        try
        {
            // Parse JSON array of ingredients
            ArrayList<IngredientDTO> ingredientsList = new ArrayList<>();

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(ingredientsListStr);

            if (root.isArray())
            {
                for (JsonNode node : root)
                {
                    IngredientDTO ingredient = new IngredientDTO();

                    ingredient.setIngredientName(node.path("name").asText());
                    ingredient.setAmount(node.path("amount").asInt());
                    ingredient.setUnit(node.path("unit").asText());
                    ingredientsList.add(ingredient);
                }
            }

            ingredientService.addIngredientListToRecipe(ingredientsList, recipeID);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * Update instructions list of a recipe
     * @param recipeID
     *      ID of recipe
     * @param instructionsListStr
     *      updated instructions list
     * @return
     *      success or failure
     */
    @ApiOperation(value = "UPDATE instructions list of a recipe", response = String.class, tags = "Recipe Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @PutMapping(path = "/recipe-management/recipes/{recipeID}/update-recipe-instructions")
    public ResponseEntity<String> updateRecipeInstructions(@PathVariable
                                                               @ApiParam(name = "recipe id", value = "id of the recipe to be updated", example = "10")
                                                               int recipeID,
                                                           @RequestParam("instructions")
                                                                @ApiParam(name = "instructions list", value = "updated list of instructions in JSON string array format",
                                                                        example = "[\n" +
                                                                                "    \"Prepare sugar and eggs.\",\n" +
                                                                                "    \"Beat together.\",\n" +
                                                                                "    \"Add water\",\n" +
                                                                                "]")
                                                                String instructionsListStr)
    {
        ArrayList<Instructions> instructionsList = new ArrayList<>();

        try
        {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode values = mapper.readTree(instructionsListStr);

            if (values.isArray())
            {
                for (JsonNode node : values)
                {
                    Instructions instructions = new Instructions();
                    instructions.setText(node.asText());
                    //instructions.setOrder(node.path("order").asInt());
                    instructionsList.add(instructions);
                }
            }

            recipeService.updateRecipeInstructions(instructionsList, recipeID);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

//    @GetMapping(path = "/recipes/{recipeName}")
//    ResponseEntity<Recipe> getRecipeByRecipeName(@PathVariable String recipeName)
//    {
//        Recipe recipe;
//
//        try
//        {
//            recipe = recipeService.getRecipeByName(recipeName);
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>((recipe), HttpStatus.OK);
//    }

    /*
     * The Response Entity type is set as <Resource>, which can handle files and images very well
     * additional header have to be set to tell the front end what type of content is being sent from the
     * backend, thus in this example headers are set.
     */
//    @GetMapping(path = "/recipe/{recipeID}/image")
//    ResponseEntity<Resource> getRecipeImage(@PathVariable int recipeID) throws SQLException //throws IOException, SQLException
//    {
//        Recipe recipe = recipeService.getRecipeByID(recipeID);
//
//        // if user id not found it cannot have an avatar associated with it
//        if (recipe == null || recipe.getImage()== null)
//        {
//            return null;
//        }
//
//        // add headers to state that a file is being downloaded
//        HttpHeaders header = new HttpHeaders();
//        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + recipe.getExtension());
//        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        header.add("Pragma", "no-cache");
//        header.add("Expires", "0");
//
//        // convert blob to bytearray and set it as response
//        int blobLength = (int)recipe.getImage().length();
//        byte[] byteArray = recipe.getImage().getBytes(1, blobLength);
//        ByteArrayResource data = new ByteArrayResource(byteArray);
//
//        // send the response entity back to the front end with the file
//        return ResponseEntity.ok()
//                .headers(header)
//                .contentLength(blobLength)
//                .contentType(MediaType.parseMediaType("image/jpeg"))
//                .body(data);
//    }

    /*
     * This is where the userdata and the image should be uploaded, you can observe that there is no @RequestBody,
     * this is because a file and data cannot be uploaded together with different formats with @RequestBody.
     * @RequestParam allows for 2 different types of data i.e. file and text. The text is entered as a JSON and then
     * converted into an Object by the Object mapper and then saved into the database.
     * Note: use form data as body for postman testing
     * first key will be avatar with associated file, and the second key will be user with input as a json
     */
    /* ignore for now
    @PostMapping(path = "/recipe/{username}/create1")
    //String createUser(@RequestParam("avatar") MultipartFile avatar, @RequestParam("user") String userString)
    ResponseEntity<String> createUserRecipe(
            @RequestParam("image") MultipartFile image,
            @RequestParam("recipe") String recipeString,
            @PathVariable String username)
    {
        // This recipe should have ID of -1
        // will be assigned in DB

        try
        {
            //recipeService.createUserRecipe(APIHelper.parseJsonNodeToRecipe(jsonNode), username);

            ObjectMapper objectMapper = new ObjectMapper();
            Recipe recipe = objectMapper.readValue(recipeString, Recipe.class);
            recipe.setExtension(image.getOriginalFilename());

            if(image != null)
            {
                byte[] file = image.getBytes();
                SerialBlob blob = new SerialBlob(file);
                Blob img = blob;
                recipe.setImage(img);
            }

            recipeService.createUserRecipe(recipe, username);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(failure, HttpStatus.BAD_REQUEST);
        }

        // Return value success or return the recipe object created?
        return new ResponseEntity<>(success, HttpStatus.CREATED);
    }*/

    /**
     * Deletes a recipe from a user's recipe list
     * @param recipeID
     *      ID of recipe to be deleted
     * @return
     *      success or failure
     */
    @ApiOperation(value = "DELETE recipe", response = String.class, tags = "Recipe Controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 500, message = "Internal Server Error.")
    })
    @DeleteMapping(path = "/recipe-management/recipes/{recipeID}")
    ResponseEntity<String> deleteRecipeByUser(@PathVariable
                                              @ApiParam(name = "recipe id", value = "id of the recipe to be deleted", example = "10")
                                              int recipeID)
    {
        try
        {
            recipeService.deleteUserRecipe(recipeID);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /*
//
//    @PutMapping(path = "/recipe/update/{recipeID}")
//    public ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe recipe, @PathVariable int recipeID)
//    {
//        Recipe updatedRecipe;
//        try
//        {
//            updatedRecipe = recipeService.updateRecipe(recipe, recipeID);
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity<>((updatedRecipe), HttpStatus.OK);
//    }
 */
}
