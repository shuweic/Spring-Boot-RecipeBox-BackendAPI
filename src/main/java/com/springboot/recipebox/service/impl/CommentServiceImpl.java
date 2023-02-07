package com.springboot.recipebox.service.impl;

import com.springboot.recipebox.entity.Comment;
import com.springboot.recipebox.entity.CommentReportStatus;
import com.springboot.recipebox.entity.Recipe;
import com.springboot.recipebox.exception.RecipeAPIException;
import com.springboot.recipebox.exception.ResourceNotFoundException;
import com.springboot.recipebox.payload.CommentDTO;
import com.springboot.recipebox.repository.CommentReportStatusRepository;
import com.springboot.recipebox.repository.RecipeRepository;
import com.springboot.recipebox.repository.CommentRepository;
import com.springboot.recipebox.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private RecipeRepository recipeRepository;

    private CommentReportStatusRepository commentReportStatusRepository;

    public CommentServiceImpl(CommentRepository commentRepository, RecipeRepository recipeRepository, CommentReportStatusRepository commentReportStatusRepository) {
        this.commentRepository = commentRepository;
        this.recipeRepository = recipeRepository;
        this.commentReportStatusRepository = commentReportStatusRepository;
    }

    @Override
    public CommentDTO createComment(int recipeId, CommentDTO commentDTO) {
        Comment comment = mapToEntity(commentDTO);

        // retrieve recipe entity by id
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(
                () -> new ResourceNotFoundException("Recipe", "id", recipeId));

        // set recipe to comment entity
        comment.setRecipe(recipe);

        // save comment entity to database
        Comment newComment = commentRepository.save(comment);

//        CommentReportStatus status = new CommentReportStatus();
//        status.setComment(comment);
//        status.setAdminReviewStatus(false);
//        commentReportStatusRepository.save(status);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDTO> getCommentsByRecipeId(int recipeId) {
        // retrieve comments by recipeId
        List<Comment> comments = commentRepository.findByRecipeId(recipeId);

        // convert list of comment  entities to list of comment DTOs
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(int recipeId, int commentId) {

        // retrieve recipe entity by id
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(
                () -> new ResourceNotFoundException("Recipe", "id", recipeId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!Objects.equals(comment.getRecipe().getId(), recipe.getId())) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to this Recipe");
        }

        return mapToDTO(comment);
    }

    @Override
    public CommentDTO updateComment(int recipeId, int commentId, CommentDTO commentRequest) {
        // retrieve recipe entity by id
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(
                () -> new ResourceNotFoundException("Recipe", "id", recipeId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!Objects.equals(comment.getRecipe().getId(), recipe.getId())) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to this Recipe");
        }

        comment.setName(commentRequest.getName());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(int recipeId, int commentId) {
        // retrieve recipe entity by id
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(
                () -> new ResourceNotFoundException("Recipe", "id", recipeId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!Objects.equals(comment.getRecipe().getId(), recipe.getId())) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to this Recipe");
        }
        commentRepository.delete(comment);

    }

    @Override
    public void adminReviewComment(int commentId, int commentStatusId) {

//        Comment comment = commentRepository.findById(commentId).orElseThrow(
//                () -> new ResourceNotFoundException("Comment", "id", commentId));
//
//        // retrieve comment by id
//        CommentReportStatus status = commentReportStatusRepository.findById(commentStatusId).orElseThrow(
//                () -> new ResourceNotFoundException("commentStatus", "commentStatusId", commentStatusId));
//
//        if (!Objects.equals(status.getComment().getId(), comment.getId())) {
//            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "Comment Status does not belong to this Comment");
//        }
//
//        status.setAdminReviewStatus(true);
//
//        commentReportStatusRepository.save(status);
    }


    private CommentDTO mapToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setName(comment.getName());
        commentDTO.setBody(comment.getBody());

        return commentDTO;
    }

    private Comment mapToEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setName(commentDTO.getName());
        comment.setBody(commentDTO.getBody());

        return comment;
    }
}
