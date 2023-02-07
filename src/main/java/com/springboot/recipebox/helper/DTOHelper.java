package com.springboot.recipebox.helper;

import com.springboot.recipebox.entity.*;
import com.springboot.recipebox.payload.CommentDTO;
import com.springboot.recipebox.payload.IngredientDTO;
import com.springboot.recipebox.payload.RecipeDTO;
import com.springboot.recipebox.payload.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class DTOHelper
{
    // convert User to DTO
    public static UserDTO mapUserToDto(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setUser_type(user.getUser_type());
        return userDto;
    }

    // convert DTO to User
    public static User mapDtoToUser(UserDTO userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setUser_type(userDto.getUser_type());
        return user;
    }

    // convert Recipe and additional lists to RecipeDTO
    public static RecipeDTO mapRecipeToDTO(Recipe recipe, List<String> instructionsList,
                                           List<IngredientRecipeMapping> ingredientRecipeMappings , List<String> tags)
    {
        RecipeDTO recipeDTO = new RecipeDTO();

        recipeDTO.setId(recipe.getId());
        recipeDTO.setTitle(recipe.getTitle());
        recipeDTO.setSummary(recipe.getSummary());
        recipeDTO.setCookingMinutes(recipe.getCookingMinutes());
        recipeDTO.setServings(recipe.getServings());
        recipeDTO.setInstructionsList(instructionsList);
        recipeDTO.setTags(tags);

        List<IngredientDTO> ingredientDTOList = new ArrayList<>();
        ingredientRecipeMappings.forEach(i -> ingredientDTOList.add(
                new IngredientDTO(i.getIngredientRecipeMappingKey().getIngredientName(), i.getAmount(), i.getUnit())));

        recipeDTO.setIngredientList(ingredientDTOList);

        return recipeDTO;
    }

    public static CommentDTO mapCommentToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setName(comment.getName());
        commentDTO.setBody(comment.getBody());

        return commentDTO;
    }

    public static Comment mapDtoToComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setName(commentDTO.getName());
        comment.setBody(commentDTO.getBody());

        return comment;
    }


}
