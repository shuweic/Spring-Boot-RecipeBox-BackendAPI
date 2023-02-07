package com.springboot.recipebox.repository;

import com.springboot.recipebox.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer>
{
    Recipe findRecipeById(int id);

    List<Recipe> findRecipeByUserUsername(String userName);


          //recipe.setTitle(newRecipe.getTitle());
//        recipe.setSummary(newRecipe.getSummary());
//        recipe.setCookingMinutes(newRecipe.getCookingMinutes());
//        recipe.setServings(newRecipe.getServings());
//        recipe.setImage(newRecipe.getImage());

    @Transactional
    @Modifying
    @Query("update Recipe r set r.title = ?1, r.summary = ?2, " +
            "r.cookingMinutes = ?3, r.servings = ?4, r.id = ?5")
    int updateGeneralInformation(String title, String summary, int cookingMinutes, int servings, int recipeID);
}
