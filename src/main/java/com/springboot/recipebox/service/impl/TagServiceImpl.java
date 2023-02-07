package com.springboot.recipebox.service.impl;

import com.springboot.recipebox.entity.Recipe;
import com.springboot.recipebox.entity.TagRecipeMapping;
import com.springboot.recipebox.entity.TagRecipeMapping.TagRecipeMappingKey;
import com.springboot.recipebox.entity.Tags;
import com.springboot.recipebox.entity.Tags.TagKey;
import com.springboot.recipebox.repository.TagRecipeMappingRepository;
import com.springboot.recipebox.repository.TagRepository;
import com.springboot.recipebox.repository.UserRepository;
import com.springboot.recipebox.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService
{
    TagRepository tagRepository;
    TagRecipeMappingRepository tagRecipeMappingRepository;

    UserRepository userRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagRecipeMappingRepository tagRecipeMappingRepository, UserRepository userRepository)
    {
        this.tagRepository = tagRepository;
        this.tagRecipeMappingRepository = tagRecipeMappingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<String> getTagNamesOfRecipe(int recipeID)
    {
        List<String> tagNames = new ArrayList<>();

        tagRecipeMappingRepository.getTagRecipeMappingByTagRecipeMappingKeyRecipeId(recipeID)
                .forEach(trm -> tagNames.add(
                        trm.getTagRecipeMappingKey().getTags().getTagKey().getName()
                ));

        return tagNames;
    }

    @Override
    public void addTagsToRecipe(List<String> tagNames, Recipe recipe)
    {
        tagNames.forEach(t -> addTagToRecipe(t, recipe));
    }

    @Override
    public void addTagToRecipe(String tagName, Recipe recipe)
    {
        // get tag object from database
        Tags tag = tagRepository.getTagsByTagKeyUserUsernameAndTagKeyName(recipe.getUser().getUsername(), tagName);

        // create tag for user if it doesn't exist yet
        if (tag == null)
        {
            tag = this.createTagForUser(recipe.getUser().getUsername(), tagName);
        }

        // create TagRecipeMapping object
        TagRecipeMapping tagRecipeMapping = new TagRecipeMapping();

        // create tag recipe mapping key
        TagRecipeMappingKey tagRecipeMappingKey = new TagRecipeMappingKey();
        tagRecipeMappingKey.setRecipe(recipe);
        tagRecipeMappingKey.setTags(tag);

        // set tag recipe mapping key
        tagRecipeMapping.setTagRecipeMappingKey(tagRecipeMappingKey);

        // save tag recipe mapping
        tagRecipeMappingRepository.save(tagRecipeMapping);
    }

    @Override
    public List<Integer> getUserRecipeIDWithTagName(String tagName, String username)
    {
        List<Integer> idList = tagRecipeMappingRepository.getUserRecipeIDWithTagName(tagName, username);
        return idList;
    }

    @Override
    public List<Integer> getAllRecipeIDWithTagName(String tagName)
    {
        List<Integer> idList = tagRecipeMappingRepository.getAllRecipeIDsWithTagName(tagName);
        return idList;
    }

    @Override
    public Tags createTagForUser(String username, String tagName)
    {
        Tags tag = new Tags();

        // create tag key
        TagKey tagKey = new TagKey();
        tagKey.setName(tagName);
        tagKey.setUser(userRepository.findUserByUsername(username).orElseThrow(() -> new RuntimeException("User invalid")));

        tag.setTagKey(tagKey);

        // check if tag already exists in database
        Tags tagRow = tagRepository.getTagsByTagKeyUserUsernameAndTagKeyName(username, tagName);
        if (tagRow == null)
            tagRepository.save(tag);

        return tag;
    }

    @Override
    public List<String> getAllTagNamesOfUser(String username)
    {
        return tagRepository.getTagNamesOfUser(username);
    }

    // only able to update tag name for a tag object
    // updates on recipe or user comes from removing recipe tag mapping or
    // deleting the tag from the user list
    @Override
    public void updateUserTag(String username, String oldTagName, String newTagName)
    {
        // can't update primary key of table
        // so strategy is to delete existing rows and mappings
        // and replace with new entries with new tag name

        // get recipe mappings first before deleting tag
        // because deleting tag will delete the mappings
        List<TagRecipeMapping> trmList = tagRecipeMappingRepository.getTagRecipeMappingsOfUserWithTagName(oldTagName, username);

        // remove old tag in database
        Tags oldTag = tagRepository.getTagsByTagKeyUserUsernameAndTagKeyName(username, oldTagName);
        tagRepository.delete(oldTag);

        Tags newTag = new Tags();

        TagKey  newTagKey = new TagKey();
        newTagKey.setName(newTagName);
        newTagKey.setUser(userRepository.findUserByUsername(username).orElseThrow(() -> new RuntimeException("User invalid")));

        newTag.setTagKey(newTagKey);

        // save new tag in database
        tagRepository.save(newTag);

        // newTag has updated info

        // add new mapping rows with updated name
        trmList.forEach(
                (trm) ->
                {
                    TagRecipeMappingKey trmKey = new TagRecipeMappingKey();
                    trmKey.setTags(newTag); // set new tag name
                    trmKey.setRecipe(trm.getTagRecipeMappingKey().getRecipe()); // recipe is still the same

                    // create new trm object
                    TagRecipeMapping newTagRecipeMapping = new TagRecipeMapping();
                    newTagRecipeMapping.setTagRecipeMappingKey(trmKey);

                    // delete tag recipe mapping with old tag name in database
                    //tagRecipeMappingRepository.delete(trm);

                    // save new tag recipe mapping in database
                    tagRecipeMappingRepository.save(newTagRecipeMapping);
                }
        );
    }

    // delete user tag
    @Override
    public void deleteUserTag(String username, String tagName)
    {
        Tags tag = tagRepository.getTagsByTagKeyUserUsernameAndTagKeyName(username, tagName);
        tagRepository.delete(tag);
    }

    // delete user recipe mapping
    @Override
    public void removeTagFromRecipe(int recipeID, String tagName)
    {
        TagRecipeMapping tagRecipeMapping = tagRecipeMappingRepository.getTagRecipeMappingRow(tagName, recipeID);
        tagRecipeMappingRepository.delete(tagRecipeMapping);
    }
}
