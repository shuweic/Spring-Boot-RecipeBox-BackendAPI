package com.springboot.recipebox.repository;

import com.springboot.recipebox.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByRecipeId(int recipeId);

}
