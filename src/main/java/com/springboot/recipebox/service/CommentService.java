package com.springboot.recipebox.service;

import com.springboot.recipebox.payload.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(int recipeId, CommentDTO commentDTO);

    List<CommentDTO> getCommentsByRecipeId(int recipeId);

    CommentDTO getCommentById(int recipeId, int commentId);

    CommentDTO updateComment(int recipeId, int commentId, CommentDTO commentRequest);

    void deleteComment(int recipeId, int commentId);

    void adminReviewComment(int commentId, int commentStatusId);
}
