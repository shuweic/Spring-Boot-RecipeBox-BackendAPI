package com.springboot.recipebox.repository;

import com.springboot.recipebox.entity.IngredientRecipeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientRecipeMappingRepository extends JpaRepository<IngredientRecipeMapping, IngredientRecipeMapping.IngredientRecipeMappingKey>
{
    List<IngredientRecipeMapping> findAllByIngredientRecipeMappingKeyRecipeId(int recipeID);
}
