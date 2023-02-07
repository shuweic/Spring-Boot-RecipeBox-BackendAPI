package com.springboot.recipebox.controller;

import com.springboot.recipebox.payload.CommentDTO;
import com.springboot.recipebox.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Recipe's Comment Controller", description = "Rest APIs related to Comment Entity!")
@RestController
@RequestMapping("/api/")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/recipes/{recipeId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable(value = "recipeId")
                                                        @ApiParam(name = "recipeId", value = "the id of recipe that comment belong to", example = "2")
                                                        int recipeId,
                                                    @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.createComment(recipeId, commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/recipes/{recipeId}/comments")
    public List<CommentDTO> getCommentsByRecipeId(@PathVariable(value = "recipeId")
                                                      @ApiParam(name = "recipeId", value = "the id of recipe that comment belong to", example = "2")
                                                      int recipeId) {
        return commentService.getCommentsByRecipeId(recipeId);
    }


    @GetMapping("/recipes/{recipeId}/comments/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable(value = "recipeId")
                                                         @ApiParam(name = "recipeId", value = "the id of recipe that comment belong to", example = "2")
                                                         int recipeId,
                                                     @PathVariable(value = "id")
                                                         @ApiParam(name = "commentId", value = "the id of comment for getting the certain data", example = "1")
                                                         int commentId) {
        CommentDTO commentDTO = commentService.getCommentById(recipeId, commentId);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);

    }

    @PutMapping("/recipes/{recipeId}/comments/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable(value = "recipeId")
                                                        @ApiParam(name = "recipeId", value = "the id of recipe that comment belong to", example = "2")
                                                        int recipeId,
                                                    @PathVariable(value = "id")
                                                        @ApiParam(name = "commentId", value = "the id of comment for getting the certain data", example = "1")
                                                        int commentId,
                                                    @RequestBody CommentDTO commentDTO) {
        CommentDTO updatedComment = commentService.updateComment(recipeId, commentId, commentDTO);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/recipes/{recipeId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "recipeId")
                                                    @ApiParam(name = "recipeId", value = "the id of recipe that comment belong to", example = "2")
                                                    int recipeId,
                                                @PathVariable(value = "id")
                                                    @ApiParam(name = "commentId", value = "the id of comment for getting the certain data", example = "1")
                                                    int commentId) {
        commentService.deleteComment(recipeId, commentId);
        return new ResponseEntity<>("comment deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/comments/{commentId}/reviewStatus/{commentStatusId}")
    public ResponseEntity<String> adminReviewComment(@PathVariable(value = "commentId")
                                                         @ApiParam(name = "commentId", value = "the id of comment for getting the certain data", example = "1")
                                                         int commentId,
                                                     @PathVariable(value = "commentStatusId")
                                                        @ApiParam(name = "commentStatusId", value = "the id of comment status for getting the certain data", example = "1") int commentStatusId) {
        commentService.adminReviewComment(commentId, commentStatusId);
        return new ResponseEntity<>("comment flagged successfully", HttpStatus.OK);
    }

}
