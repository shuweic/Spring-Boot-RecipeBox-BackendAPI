package com.springboot.recipebox.repository;

import com.springboot.recipebox.entity.TagRecipeMapping;
import com.springboot.recipebox.entity.TagRecipeMapping.TagRecipeMappingKey;
import com.springboot.recipebox.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRecipeMappingRepository extends JpaRepository<TagRecipeMapping, TagRecipeMappingKey>
{
    List<TagRecipeMapping> getTagRecipeMappingByTagRecipeMappingKeyRecipeId(int recipeID);

    @Query("select t.tagRecipeMappingKey.recipe.id " +
            "from TagRecipeMapping t " +
            "where t.tagRecipeMappingKey.tags.tagKey.name = ?1 " +
            "and t.tagRecipeMappingKey.recipe.user.username = ?2")
    List<Integer> getUserRecipeIDWithTagName(String tagName, String username);

    @Query("select t.tagRecipeMappingKey.recipe.id " +
            "from TagRecipeMapping t " +
            "where t.tagRecipeMappingKey.tags.tagKey.name = ?1")
    List<Integer> getAllRecipeIDsWithTagName(String tagName);

    @Query("select t " +
            "from TagRecipeMapping t " +
            "where t.tagRecipeMappingKey.tags.tagKey.name = ?1 " +
            "and t.tagRecipeMappingKey.recipe.user.username = ?2")
    List<TagRecipeMapping> getTagRecipeMappingsOfUserWithTagName(String tagName, String username);

    @Query("select t from TagRecipeMapping t " +
            "where t.tagRecipeMappingKey.tags.tagKey.name = ?1 " +
            "and t.tagRecipeMappingKey.recipe.id = ?2")
    TagRecipeMapping getTagRecipeMappingRow(String tagName, int recipeID);
}
