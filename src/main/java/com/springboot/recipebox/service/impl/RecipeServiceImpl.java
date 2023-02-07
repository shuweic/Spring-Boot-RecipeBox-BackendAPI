package com.springboot.recipebox.service.impl;

import com.springboot.recipebox.entity.*;
import com.springboot.recipebox.exception.ResourceNotFoundException;
import com.springboot.recipebox.helper.DTOHelper;
import com.springboot.recipebox.payload.RecipeDTO;
import com.springboot.recipebox.payload.RecipeNameDTO;
import com.springboot.recipebox.repository.*;
import com.springboot.recipebox.service.RecipeService;
import com.springboot.recipebox.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService
{
    RecipeRepository recipeRepository;

    UserRepository userRepository;

    InstructionsRepository instructionsRepository;

    IngredientRecipeMappingRepository ingredientRecipeMappingRepository;

    UserSavedRecipeMappingRepository userSavedRecipeMappingRepository;

    TagService tagService;

    @Autowired
    RecipeServiceImpl(RecipeRepository recipeRepository, UserRepository userRepository,
                      InstructionsRepository instructionsRepository, IngredientRecipeMappingRepository ingredientRecipeMappingRepository,
                      UserSavedRecipeMappingRepository userSavedRecipeMappingRepository, TagService tagService)
    {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.instructionsRepository = instructionsRepository;
        this.ingredientRecipeMappingRepository = ingredientRecipeMappingRepository;
        this.userSavedRecipeMappingRepository = userSavedRecipeMappingRepository;
        this.tagService = tagService;
    }

    @Override
    public int createUserBlankRecipe(String recipeName, String username)
    {
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeName);

        Optional<User> user = userRepository.findUserByUsername(username);
        recipe.setUser(user.get());

        return recipeRepository.save(recipe).getId();
    }

    @Override
    public void deleteUserRecipe(int recipeID)
    {
        Recipe recipe = recipeRepository.findById(recipeID).orElseThrow(() -> new ResourceNotFoundException("Recipe not found", "id", recipeID));
        recipeRepository.delete(recipe);
    }

    @Override
    public void updateRecipeGeneralInfo(Recipe newRecipe, int recipeID)
    {
        Recipe recipe = recipeRepository.findRecipeById(recipeID);

        recipe.setTitle(newRecipe.getTitle());
        recipe.setSummary(newRecipe.getSummary());
        recipe.setCookingMinutes(newRecipe.getCookingMinutes());
        recipe.setServings(newRecipe.getServings());

//        recipeRepository.updateGeneralInformation(
//                newRecipe.getTitle(),
//                newRecipe.getSummary(),
//                newRecipe.getCookingMinutes(),
//                newRecipe.getServings(),
//                recipeID
//        );


//        recipe.setImage(newRecipe.getImage());
    }

    @Override
    public void updateRecipeInstructions(List<Instructions> instructionsList, int recipeID)
    {
        // Delete old instructions
        List<Instructions> oldInstructionsList = instructionsRepository.getAllByRecipeId(recipeID);

        if (oldInstructionsList != null)
            oldInstructionsList.forEach(i -> instructionsRepository.delete(i));

        Recipe recipe = recipeRepository.findRecipeById(recipeID);

        int y = 1;
        for (Instructions i: instructionsList)
        {
            Instructions instructions = new Instructions();
            instructions.setText(i.getText());
            instructions.setOrder(y++);
            instructions.setRecipe(recipe);
            instructionsRepository.save(instructions);
        }
    }

    @Override
    public List<RecipeDTO> getAllRecipesCreatedByUser(String username)
    {
        List<Integer> recipeIDs = new ArrayList<>();
        recipeRepository.findRecipeByUserUsername(username).forEach(recipe -> recipeIDs.add(recipe.getId()));

        List<RecipeDTO> recipeDTOList = new ArrayList<>();
        recipeIDs.forEach(id -> recipeDTOList.add(this.getRecipeDTOByID(id)));

        return  recipeDTOList;
    }

    @Override
    public RecipeDTO getRecipeDTOByID(int recipeID)
    {
        Recipe recipe = recipeRepository.findRecipeById(recipeID);

//        private String title;
//        private String summary;
//        private int cookingMinutes;
//        private int servings;
//        private List<String> instructionsList;
//        private List<Ingredient> ingredientList;
//        private List<String> tags;

        // get instructions list
        List<String> instructionsList = new ArrayList<>();
        instructionsRepository.getAllByRecipeId(recipeID).forEach(i -> instructionsList.add(i.getText()));

        // get ingredient rows from mapping table
        List<IngredientRecipeMapping> ingredientRecipeMappings = ingredientRecipeMappingRepository.findAllByIngredientRecipeMappingKeyRecipeId(recipeID);

        // get tags list
        List<String> tags;
        tags = tagService.getTagNamesOfRecipe(recipeID);

        RecipeDTO recipeDTO = DTOHelper.mapRecipeToDTO(recipe, instructionsList, ingredientRecipeMappings, tags);

        return recipeDTO;
    }

    @Override
    public Recipe getRecipeByID(int recipeID)
    {
        Recipe recipe = recipeRepository.findRecipeById(recipeID);
        return recipe;
    }

    // save recipe related methods

    @Override
    public void addRecipeToUserSavedRecipeList(int recipeID, String username)
    {
        Recipe recipe = recipeRepository.findRecipeById(recipeID);

        UserSavedRecipeMapping.UserSavedRecipeMappingKey userSavedRecipeMappingKey = new UserSavedRecipeMapping.UserSavedRecipeMappingKey();
        userSavedRecipeMappingKey.setRecipe(recipe);

        Optional<User> user = userRepository.findUserByUsername(username);
        userSavedRecipeMappingKey.setUser(user.get());

        UserSavedRecipeMapping userSavedRecipeMapping = new UserSavedRecipeMapping(userSavedRecipeMappingKey);

        userSavedRecipeMappingRepository.save(userSavedRecipeMapping);
    }

    @Override
    public List<RecipeNameDTO> getAllSavedRecipeNameAndIDsByUser(String username)
    {
        List<RecipeNameDTO> recipeNameDTOList = new ArrayList<>();
        userSavedRecipeMappingRepository.findSavedRecipeMappingsByUsername(username).forEach(m ->
                recipeNameDTOList.add(
                        new RecipeNameDTO(
                                m.getUserSavedRecipeMappingKey().getRecipe().getId(),
                                m.getUserSavedRecipeMappingKey().getRecipe().getTitle())
                ));

        return recipeNameDTOList;
    }

    // tag-related methods

    @Override
    public void addTagsToRecipe(List<String> tagNames, int recipeID)
    {
        Recipe recipe = recipeRepository.findRecipeById(recipeID);
        tagService.addTagsToRecipe(tagNames, recipe);
    }

    // return recipe list of user that has the given tag name
    @Override
    public List<RecipeDTO> getUserRecipeListWithTagName(String username, String tagName)
    {
        List<RecipeDTO> recipeDTOList = new ArrayList<>();
        tagService.getUserRecipeIDWithTagName(tagName, username).forEach(id -> recipeDTOList.add(this.getRecipeDTOByID(id)));
        return recipeDTOList;
    }

    @Override
    public List<RecipeDTO> getAllRecipeWithTagName(String tagName)
    {
        List<RecipeDTO> recipeDTOList = new ArrayList<>();
        tagService.getAllRecipeIDWithTagName(tagName).forEach(id -> recipeDTOList.add(this.getRecipeDTOByID(id)));
        return recipeDTOList;
    }
}
