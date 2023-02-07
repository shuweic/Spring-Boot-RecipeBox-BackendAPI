package com.springboot.recipebox.service.impl;

import com.springboot.recipebox.entity.IngredientRecipeMapping;
import com.springboot.recipebox.entity.IngredientRecipeMapping.IngredientRecipeMappingKey;
import com.springboot.recipebox.entity.Recipe;
import com.springboot.recipebox.payload.IngredientDTO;
import com.springboot.recipebox.repository.IngredientRecipeMappingRepository;
import com.springboot.recipebox.repository.RecipeRepository;
import com.springboot.recipebox.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Service for mapping/handling ingredients to recipe
@Service
public class IngredientServiceImpl implements IngredientService
{
    RecipeRepository recipeRepository;

    IngredientRecipeMappingRepository ingredientRecipeMappingRepository;

    @Autowired
    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientRecipeMappingRepository ingredientRecipeMappingRepository)
    {
        this.recipeRepository = recipeRepository;
        this.ingredientRecipeMappingRepository = ingredientRecipeMappingRepository;
    }

    // a note about this method
    // the requirements of the app was to do a PUT request that will update the current ingredients list
    // this seems to involve deleting or adding to the previous list
    // which can get confusing
    // a recommendation can be is to do one add/delete/update request each time the user adds/deletes/updates an ingredient in the recipe template
    @Override
    public void addIngredientListToRecipe(List<IngredientDTO> ingredientList, int recipeID)
    {
        // get recipe mapping rows from database
        // this is the list of previous ingredients of the recipe
        // can be empty, same, or less than the new list
        List<IngredientRecipeMapping> ingredientRecipeMappingRows = ingredientRecipeMappingRepository.findAllByIngredientRecipeMappingKeyRecipeId(recipeID);

        // delete all the ingredients mapping rows
        ingredientRecipeMappingRows.forEach(ingredientRecipeMapping -> ingredientRecipeMappingRepository.delete(ingredientRecipeMapping));

        // add new rows from the new ingredients list
        ingredientList.forEach(
                ingredientDTO ->
                {
                    IngredientRecipeMapping ingredientRecipeMapping = new IngredientRecipeMapping();

                    // create new key
                    IngredientRecipeMappingKey ingredientRecipeMappingKey = new IngredientRecipeMappingKey();
                    ingredientRecipeMappingKey.setRecipe(recipeRepository.findRecipeById(recipeID));
                    ingredientRecipeMappingKey.setIngredientName(ingredientDTO.getIngredientName());

                    // set key
                    ingredientRecipeMapping.setIngredientRecipeMappingKey(ingredientRecipeMappingKey);


                    // set other fields
                    ingredientRecipeMapping.setUnit(ingredientDTO.getUnit());
                    ingredientRecipeMapping.setAmount(ingredientDTO.getAmount());

                    // save to database
                    ingredientRecipeMappingRepository.save(ingredientRecipeMapping);
                });
    }
}
